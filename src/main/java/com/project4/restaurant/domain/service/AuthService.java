package com.project4.restaurant.domain.service;

import com.project4.restaurant.app.dto.authentication.*;
import com.project4.restaurant.app.response.authen.RefreshTokenResponse;
import com.project4.restaurant.app.response.authen.TokenResponse;
import com.project4.restaurant.app.response.authen.VerifyOtpResponse;
import com.project4.restaurant.domain.Constants;
import com.project4.restaurant.domain.core.exception.AccountDeletedException;
import com.project4.restaurant.domain.core.exception.BadRequestException;
import com.project4.restaurant.domain.core.exception.UnAuthenticationException;
import com.project4.restaurant.domain.core.exception.base.ErrorMessage;
import com.project4.restaurant.domain.entity.type.AccountRole;
import com.project4.restaurant.domain.entity.type.AccountState;
import com.project4.restaurant.domain.entity.user.UserAccount;
import com.project4.restaurant.domain.pojo.OtpInfo;
import com.project4.restaurant.domain.provider.JwtTokenProvider;
import com.project4.restaurant.domain.repository.UserAccountRepository;
import com.project4.restaurant.domain.service.base.BaseService;
import com.project4.restaurant.domain.storage.UserStorage;
import com.project4.restaurant.domain.util.CacheKey;
import com.project4.restaurant.domain.util.GeneratorUtil;
import com.project4.restaurant.domain.util.RemoteCache;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class AuthService extends BaseService {
  private static final long USER_TTL_DAYS = 1; //Thoi gian song register 1 day
  private static final long OTP_EXPIRATION_MINUTES = 5;
  private static final int MAX_ATTEMPTS = 5;
  private static final long LOCK_TIME_IN_MINUTE = 30; // thời gian khóa tài khoản, tính bằng phút
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final RemoteCache remoteCache;
  private final UserAccountService userAccountService;
  private final CacheKey cacheKey;
  private final BaseService baseService;
  private final Validator validator;
  private final UserStorage userStorage;
  private final PasswordEncoder passwordEncoder;
  private final OtpService otpService;
  @Lazy
  @Autowired
  private AuthService self;

  public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, RemoteCache remoteCache, UserAccountService userAccountService, CacheKey cacheKey, BaseService baseService, Validator validator, UserStorage userStorage, PasswordEncoder passwordEncoder, OtpService otpService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.remoteCache = remoteCache;
    this.userAccountService = userAccountService;
    this.cacheKey = cacheKey;
    this.baseService = baseService;
    this.validator = validator;
    this.userStorage = userStorage;
    this.passwordEncoder = passwordEncoder;
    this.otpService = otpService;
  }

  public void register(RegisterDto registerDto) {
    if (registerDto != null) {
      registerDto.formatAllField();
    } else {
      throw new BadRequestException("Thông tin đăng ký không được để trống");
    }
    Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
    if (!violations.isEmpty()) {
      throw new BadRequestException(processMessageError(violations.stream().map(ConstraintViolation::getMessage).toList()));
    }

    String email = registerDto.getEmail();

    UserAccount user = userStorage.findByUsername(registerDto.getUsername());
    if (user != null) {
      if (user.getState() == AccountState.DELETED) {
        throw new AccountDeletedException(ErrorMessage.USER_DELETED);
      }
      throw new BadRequestException("Username đã tồn tại");
    }

    if (userStorage.existByEmail(registerDto.getEmail())) {
      throw new BadRequestException("Email đã tồn tại");
    }

    if (userStorage.existsByPhoneNumber(registerDto.getPhoneNumber())) {
      throw new BadRequestException("Số điện thoại cá nhân đã tồn tại");
    }

    //luu lai thong tin dang key vao cache
    RBucket<RegisterDto> registerBucket = redissonClient.getBucket(cacheKey.genRegisterKey(email));
    RBucket<String> otpBucket = redissonClient.getBucket(cacheKey.genOtpRegisterKey(email));

    String otpCode = String.format("%06d", GeneratorUtil.genOtp());
    registerBucket.set(registerDto);
    registerBucket.expire(Duration.ofDays(USER_TTL_DAYS));
    otpBucket.set(otpCode);
    otpBucket.expire(Duration.ofMinutes(OTP_EXPIRATION_MINUTES)); // Đặt TTL cho OTP

    log.debug("=====send to email: {} with otp: {}", email, otpCode);
    OtpInfo otpInfo = new OtpInfo();
    otpInfo.setTo(email);
    otpInfo.setOtpCode(otpCode);
    otpInfo.setType(OtpInfo.Type.NEW_ACCOUNT);
    otpInfo.setMethod(OtpInfo.Method.EMAIL);
    otpService.processOtp(otpInfo);
  }

  public boolean registerVerifyOtp(VerifyOtpDto verifyOtpDto) {
    if (verifyOtpDto == null) {
      throw new BadRequestException("Vui lòng nhập thông tin xác thực otp");
    }
    verifyOtpDto.formatAllField();
    Set<ConstraintViolation<VerifyOtpDto>> violations = validator.validate(verifyOtpDto);
    if (!violations.isEmpty()) {
      throw new BadRequestException(processMessageError(violations.stream().map(ConstraintViolation::getMessage).toList()));
    }
    String email = verifyOtpDto.getEmail();
    String otpCode = verifyOtpDto.getOtpCode();
    RBucket<RegisterDto> registerBucket = redissonClient.getBucket(cacheKey.genRegisterKey(email));
    RBucket<String> otpBucket = redissonClient.getBucket(cacheKey.genOtpRegisterKey(email));
    RMap<String, Object> userMetadata = redissonClient.getMap(cacheKey.genUserMetadataRegister(email));

    // Kiểm tra số lần nhập OTP sai
    int failedAttempts = userMetadata.containsKey(FAILED_ATTEMPTS_KEY) ? (Integer) userMetadata.get(FAILED_ATTEMPTS_KEY) : 0;

    if (!otpBucket.get().equals(otpCode)) {
      failedAttempts++;
      if (failedAttempts > MAX_ATTEMPTS) {
        userMetadata.remove(FAILED_ATTEMPTS_KEY); // Xóa số lần nhập sai sau khi block
        userMetadata.remove(BLOCK_TIME_KEY);
        registerBucket.delete();
        otpBucket.delete();
        userMetadata.delete();
        throw new BadRequestException("Bạn đã nhập sai OTP quá " + MAX_ATTEMPTS + " lần. Vui lòng thử lại sau");
      }
      userMetadata.put(FAILED_ATTEMPTS_KEY, failedAttempts);
      userMetadata.expire(Duration.ofHours(LOCK_TIME_IN_MINUTE)); // Đặt TTL cho số lần nhập sai
      throw new BadRequestException("OTP đã hết hạn hoặc không tồn tại.");
    }

    // Xóa thông tin liên quan đến số lần nhập sai và block time nếu OTP đúng
    userMetadata.remove(FAILED_ATTEMPTS_KEY);
    userMetadata.remove(BLOCK_TIME_KEY);

    RegisterDto registerDto = registerBucket.get();
    if (registerDto == null) {
      throw new BadRequestException("Người dùng không tồn tại");
    }

    // Lưu người dùng vào database sau khi OTP được xác nhận
    self.saveAgency(registerDto);

    // Xóa thông tin liên quan đến người dùng từ Redis
    registerBucket.delete();
    userMetadata.delete();
    return true;
  }

  public TokenResponse login(LoginDto loginDto) {
    if (loginDto == null) {
      throw new BadRequestException("Tên đăng nhập không được để trống");
    }
    loginDto.formatAllField();
    Set<ConstraintViolation<LoginDto>> violations = validator.validate(loginDto);
    if (!violations.isEmpty()) {
      throw new BadRequestException(baseService.processMessageError(violations.stream().map(ConstraintViolation::getMessage).toList()));
    }
    if (isBlocked(loginDto.getUsername())) {
      throw new BadRequestException(Constants.ACCOUNT_BLOCK_MESSAGE);
    }
    UserAccount userCheck = userStorage.findByUsername(loginDto.getUsername());
    if (userCheck == null) {
      throw new BadRequestException("Sai tên đăng nhập hoặc mật khẩu");
    }
    if(!passwordEncoder.matches(loginDto.getPassword(), userCheck.getPassword())) {
      throw new BadRequestException("Sai tên đăng nhập hoặc mật khẩu");
    }
    if (userCheck.getState() == AccountState.DELETED) {
      throw new AccountDeletedException("Tài khoản người dùng đã bị xóa");
    }
    if (!userCheck.getState().equals(AccountState.ACTIVE)){
      throw new BadRequestException(Constants.ACCOUNT_BANNED_MESSAGE);
    }
    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginDto.getUsername(), loginDto.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserAccount user = (UserAccount) authentication.getPrincipal();
      String accessToken = jwtTokenProvider.createAccessToken(loginDto.getUsername(), user.getId(), user.getRole());
      String refreshToken = "";
      if (Boolean.TRUE.equals(loginDto.getRememberMe())) {
        refreshToken = jwtTokenProvider.createRefreshToken(loginDto.getUsername(), user.getId(), user.getRole());
        saveRefreshToken(loginDto.getUsername(), refreshToken, jwtTokenProvider.getExpiredRefreshToken());
      }
      loginSucceeded(loginDto.getUsername());
      return new TokenResponse(
          accessToken, refreshToken, user.getUsername(), user.getRole());
    } catch (Exception e) {
      loginFailed(loginDto.getUsername());
      throw e;
    }
  }

  public void loginFailed(String username) {
    RMapCache<String, Integer> attemptsCache = remoteCache.getMapCache(cacheKey.loginAttempts());
    Integer attempts = attemptsCache.getOrDefault(username, 0);
    attempts++;
    if (attempts >= MAX_ATTEMPTS) {
      attemptsCache.put(username, attempts, LOCK_TIME_IN_MINUTE, TimeUnit.MINUTES);
    } else {
      attemptsCache.put(username, attempts);
    }
  }

  public RefreshTokenResponse refreshToken(RefreshTokenDto refreshTokenDto) {
    try {
      String username = jwtTokenProvider.getUsername(refreshTokenDto.getRefreshToken());
      UserAccount user = (UserAccount) userAccountService.loadUserByUsername(username);
      if (user == null) {
        throw new UnAuthenticationException("Token không hợp lệ");
      }

      String storedRefreshToken = getRefreshToken(username);
      if (!refreshTokenDto.getRefreshToken().equals(storedRefreshToken)) {
        throw new UnAuthenticationException("Token không hợp lệ");
      }

      // Tạo access token mới
      String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getId(), user.getRole());
      String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername(), user.getId(), user.getRole());
      saveRefreshToken(user.getUsername(), refreshToken, jwtTokenProvider.getExpiredRefreshToken());
      return RefreshTokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    } catch (Exception e) {
      throw new UnAuthenticationException("Token không hợp lệ");
    }
  }

  public void logout(LogoutDto logoutDto) {
    if (logoutDto.getRefreshToken() != null && !logoutDto.getRefreshToken().trim().isEmpty()) {
      String username = jwtTokenProvider.getUsername(logoutDto.getRefreshToken());
      deleteRefreshToken(username);
    }
  }

  public void loginSucceeded(String username) {
    RMapCache<String, Integer> attemptsCache = remoteCache.getMapCache(cacheKey.loginAttempts());
    attemptsCache.remove(username);
  }

  public boolean isBlocked(String username) {
    RMapCache<String, Integer> attemptsCache = remoteCache.getMapCache(cacheKey.loginAttempts());
    Integer attempts = attemptsCache.get(username);
    return attempts != null && attempts >= MAX_ATTEMPTS;
  }

  public void saveRefreshToken(String username, String refreshToken, long durationInMs) {
    remoteCache.setInMs(cacheKey.genRefreshTokenKey(username), refreshToken, durationInMs);
  }

  public String getRefreshToken(String username) {
    return remoteCache.get(cacheKey.genRefreshTokenKey(username));
  }

  public void deleteRefreshToken(String username) {
    remoteCache.del(cacheKey.genRefreshTokenKey(username));
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public void saveAgency(RegisterDto registerDto) {
    UserAccount user = new UserAccount();
    user.setId(generateSequence(UserAccount.SEQUENCE_NAME));
    user.setUsername(registerDto.getUsername());
    user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
    user.setFullName(registerDto.getFullName());
    user.setEmail(registerDto.getEmail());
    user.setPhoneNumber(registerDto.getPhoneNumber());
    user.setState(AccountState.ACTIVE);
    user.setRole(AccountRole.CUSTOMER);
    userStorage.save(user);
  }
}