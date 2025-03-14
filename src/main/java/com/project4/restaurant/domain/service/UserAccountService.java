package com.project4.restaurant.domain.service;

import com.project4.restaurant.app.dto.authentication.ChangePasswordDto;
import com.project4.restaurant.app.dto.authentication.ForgotPasswordDto;
import com.project4.restaurant.app.dto.user.UpdateProfileDto;
import com.project4.restaurant.app.dto.user.UserAccountCreateDto;
import com.project4.restaurant.app.dto.user.UserAccountUpdateDto;
import com.project4.restaurant.app.dto.user.UserUpdateStateDto;
import com.project4.restaurant.app.dto.authentication.ResetPasswordDto;
import com.project4.restaurant.app.response.authen.ForgotPasswordTokenResponse;
import com.project4.restaurant.app.response.user.UserAccountResponse;
import com.project4.restaurant.domain.core.exception.AccountDeletedException;
import com.project4.restaurant.domain.core.exception.BadRequestException;
import com.project4.restaurant.domain.entity.type.AccountState;
import com.project4.restaurant.domain.entity.user.UserAccount;
import com.project4.restaurant.domain.core.exception.ResourceFoundException;
import com.project4.restaurant.domain.pojo.LeadershipDropListResponse;
import com.project4.restaurant.domain.pojo.OtpInfo;
import com.project4.restaurant.domain.service.base.BaseService;
import com.project4.restaurant.domain.util.CacheKey;
import com.project4.restaurant.domain.util.GeneratorUtil;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.ConstraintViolation;
import lombok.extern.log4j.Log4j2;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

@Service
@Log4j2
public class UserAccountService extends BaseService implements UserDetailsService {
  private static final int MAX_FAILED_ATTEMPTS = 5; //so lan verify toi da cho mot cai ma
  private static final long USER_TTL_DAYS = 1; //Thoi gian song register 1 day
  private static final long BLOCK_TIME_HOURS = 3; //// thoi gian doi mat khau
  private static final long OTP_EXPIRATION_MINUTES = 5;
  @Autowired
  PasswordEncoder encoder;

  @Autowired
  private RedissonClient redissonClient;

  @Autowired
  private OtpService otpService;

  @Autowired
  private CacheKey cacheKey;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public Page<UserAccountResponse> getAll(String search, AccountState state, Pageable pageable) {
    List<Criteria> criteriaList = new ArrayList<>();
    if(search != null) {
      Criteria searchCriteria = new Criteria().orOperator(
          Criteria.where("username").regex(".*" + search + ".*", "i"),
          Criteria.where("fullName").regex(".*" + search + ".*", "i"),
          Criteria.where("phoneNumber").regex(".*" + search + ".*", "i"),
          Criteria.where("email").regex(".*" + search + ".*", "i")
      );
      criteriaList.add(searchCriteria);
    }
    if(state != null) {
      criteriaList.add(Criteria.where("state").is(state));
    }
    criteriaList.add(Criteria.where("state").ne(AccountState.DELETED));
    Query query = new Query();
    query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
    query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<UserAccount> userAccountPage = userStorage.findAll(query, pageable);
    return mapperUtil.mapEntityPageIntoDtoPage(userAccountPage, UserAccountResponse.class);
  }

  public UserAccountResponse getUserAccountDetail(Long userId) {
    UserAccount userAccount = userStorage.findById(userId);
    return modelMapper.toUserAccountResponse(userAccount);
  }

  public UserAccountResponse createAccount(UserAccountCreateDto dto) {
    boolean existedEmail = userStorage.existByEmail(dto.getEmail());
    if (existedEmail) {
      throw new ResourceFoundException("Email: " + dto.getEmail() + " đã tồn tại");
    }
    UserAccount userAccount = new UserAccount();
    userAccount.setFullName(dto.getFullName());
    userAccount.setUsername(dto.getUsername());
    userAccount.setPassword(encoder.encode(dto.getPassword()));
    userAccount.setPhoneNumber(dto.getPhoneNumber());
    userAccount.setEmail(dto.getEmail());
    userAccount.setRole(dto.getRole());
    userAccount.setState(dto.getState());
    userAccount.setCreatedAt(System.currentTimeMillis());
    userAccount.setUpdatedAt(System.currentTimeMillis());
    userStorage.save(userAccount);
    return modelMapper.toUserAccountResponse(userAccount);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public UserAccountResponse updateAccount(Long userId, UserAccountUpdateDto dto) {
    UserAccount userAccount = userStorage.findById(userId);
    userAccount.setFullName(dto.getFullName());
    if(!userAccount.getUsername().equals(dto.getUsername())) {
    }
    userAccount.setUsername(dto.getUsername());
    if(dto.getPassword() != null && !dto.getPassword().isEmpty()) {
      userAccount.setPassword(encoder.encode(dto.getPassword()));
    }
    if(userAccount.getPhoneNumber() == null || !userAccount.getPhoneNumber().equals(dto.getPhoneNumber())) {
    }
    userAccount.setPhoneNumber(dto.getPhoneNumber());
    if(userAccount.getEmail() == null || !userAccount.getEmail().equals(dto.getEmail())) {
      boolean isExistEmail = userStorage.existByEmail(dto.getEmail());
      if (isExistEmail) {
        throw new ResourceFoundException("Email: " + dto.getEmail() + " đã tồn tại");
      }
    }
    userAccount.setEmail(dto.getEmail());
    userAccount.setRole(dto.getRole());
    userAccount.setState(dto.getState());
    userAccount.setUpdatedAt(System.currentTimeMillis());
    userStorage.save(userAccount);
    return modelMapper.toUserAccountResponse(userAccount);
  }

  public Boolean deleteAccount(Long userId) {
    UserAccount userAccount = userStorage.findById(userId);
    if(userAccount.getUsername().equals("admin")) {
      throw new BadRequestException("Không được xóa tài khoản Admin");
    }
    else{
      if(!userAccount.getState().equals(AccountState.ACTIVE)){
        userAccount.setState(AccountState.DELETED);
        userStorage.save(userAccount);
      }
      else{
        throw new BadRequestException("Không được xóa tài khoản khi đang ở trạng thái hoạt động");
      }
    }
    return true;
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public Boolean updateUserState(Long userId, UserUpdateStateDto userUpdateStateDto) {
    UserAccount userAccount = userStorage.findById(userId);
    if(userAccount.getUsername().equals("admin")) {
      throw new BadRequestException("Không thể thay đổi trạng thái của Admin");
    }
    userAccount.setState(userUpdateStateDto.getUserState());
    userAccount.setUpdatedAt(System.currentTimeMillis());
    userStorage.save(userAccount);
    return true;
  }

  public void forgotPassword(ForgotPasswordDto forgotPasswordDto) {
    if(forgotPasswordDto == null) {
      throw new BadRequestException("Email không được để trống");
    }
    forgotPasswordDto.formatAllField();
    Set<ConstraintViolation<ForgotPasswordDto>> violations = validator.validate(forgotPasswordDto);
    if (!violations.isEmpty()) {
      throw new BadRequestException(processMessageError(violations.stream().map(ConstraintViolation::getMessage).toList()));
    }
    String email = forgotPasswordDto.getEmail();
    UserAccount user = userStorage.findByEmail(email);
    if(user == null) {
      throw new BadRequestException("Email không tồn tại");
    }
    if (user.getState() == AccountState.DELETED) {
      throw new AccountDeletedException("Tài khoản người dùng đã bị xóa");
    }
    RBucket<ForgotPasswordDto> registerBucket = redissonClient.getBucket(cacheKey.gentUserForgotPasswordKey(email));
    RBucket<String> otpBucket = redissonClient.getBucket(cacheKey.genOtpForgotPassword(email));
    RMap<String, Object> userMetadata = redissonClient.getMap(cacheKey.genUserMetadataForgotPassword(email));

    String otpCode = String.format("%06d", GeneratorUtil.genOtp());
    registerBucket.set(forgotPasswordDto);
    registerBucket.expire(Duration.ofDays(USER_TTL_DAYS));
    otpBucket.set(otpCode);
    otpBucket.expire(Duration.ofMinutes(OTP_EXPIRATION_MINUTES)); // Đặt TTL cho OTP

    // Cập nhật số lần gửi lại OTP và thời gian gửi
//    userMetadata.put(RESEND_COUNT_KEY, resendCount + 1);
    //userMetadata.put(LAST_SENT_TIME_KEY, LocalDateTime.now());
    //userMetadata.expire(Duration.ofDays(USER_TTL_DAYS)); // Đặt TTL cho metadata

    log.debug("=====send email: {} with otp: {}", email, otpCode);
    OtpInfo otpInfo = new OtpInfo();
    otpInfo.setTo(email);
    otpInfo.setOtpCode(otpCode);
    otpInfo.setType(OtpInfo.Type.FORGET_PASSWORD);
    otpInfo.setMethod(OtpInfo.Method.EMAIL);
    otpService.processOtp(otpInfo);
  }

  public ForgotPasswordTokenResponse forgotPasswordVerifyOtp(String email, String otpCode) {
    if(!userStorage.existByEmail(email)){
      throw new BadRequestException("Email không tồn tại");
    }
    RBucket<ForgotPasswordDto> registerBucket = redissonClient.getBucket(cacheKey.gentUserForgotPasswordKey(email));
    RBucket<String> otpBucket = redissonClient.getBucket(cacheKey.genOtpForgotPassword(email));
    RMap<String, Object> userMetadata = redissonClient.getMap(cacheKey.genUserMetadataForgotPassword(email));

    if(!otpBucket.isExists()) {
      throw new BadRequestException("OTP đã hết hạn hoặc không tồn tại.");
    }
    // Kiểm tra số lần nhập OTP sai
    int failedAttempts = userMetadata.containsKey(FAILED_ATTEMPTS_KEY) ? (Integer) userMetadata.get(FAILED_ATTEMPTS_KEY) : 0;

    if (!otpBucket.get().equals(otpCode)) {
      failedAttempts++;
      if (failedAttempts > MAX_FAILED_ATTEMPTS) {
        userMetadata.remove(FAILED_ATTEMPTS_KEY); // Xóa số lần nhập sai sau khi block
        userMetadata.remove(BLOCK_TIME_KEY);
        registerBucket.delete();
        otpBucket.delete();
        userMetadata.delete();
        throw new BadRequestException("Bạn đã nhập sai OTP quá " + MAX_FAILED_ATTEMPTS + " lần. Vui lòng thử lại sau");
      }
      userMetadata.put(FAILED_ATTEMPTS_KEY, failedAttempts);
      userMetadata.expire(Duration.ofHours(BLOCK_TIME_HOURS)); // Đặt TTL cho số lần nhập sai
      throw new BadRequestException("OTP đã hết hạn hoặc không tồn tại.");
    }

    // Xóa thông tin liên quan đến số lần nhập sai và block time nếu OTP đúng
    userMetadata.remove(FAILED_ATTEMPTS_KEY);
    userMetadata.remove(BLOCK_TIME_KEY);

    // Xóa thông tin liên quan đến người dùng từ Redis
    registerBucket.delete();
    otpBucket.delete();
    userMetadata.delete();
    ForgotPasswordTokenResponse tokenResponse = new ForgotPasswordTokenResponse();
    tokenResponse.setToken(GeneratorUtil.generateUuid());
    RBucket<String> resetToken = redissonClient.getBucket(cacheKey.genTokenResetPassword(email));
    // set token
    resetToken.set(tokenResponse.getToken());
    // set expire time
    resetToken.expire(Duration.ofDays(USER_TTL_DAYS));
    return tokenResponse;
  }

  public void resetPassword(ResetPasswordDto resetPasswordDto) {
    RBucket<String> resetToken = redissonClient.getBucket(cacheKey.genTokenResetPassword(resetPasswordDto.getEmail()));
    if (!resetPasswordDto.getPassword().equals(resetPasswordDto.getConfirmPassword())) {
      throw new BadRequestException("Mật khẩu không trùng khớp");
    }
    if (!resetToken.isExists()) {
      throw new BadRequestException("Reset token không tồn tại");
    }
    if (!resetToken.get().equals(resetPasswordDto.getToken())) {
      throw new BadRequestException("Reset token không trùng khớp");
    }
    UserAccount user = userStorage.findByEmail(resetPasswordDto.getEmail());
    if (Objects.isNull(user)) {
      throw new BadRequestException("User không tồn tại");
    }
    user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
    userStorage.save(user);
    resetToken.delete();
    RBucket<UserAccount> userCache = redissonClient.getBucket(cacheKey.genCacheUserDetailKey(resetPasswordDto.getEmail()));
    userCache.delete();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserAccount userAccount = userStorage.findByUsername(username);
    if(userAccount == null) {
      throw new UsernameNotFoundException("Tài khoản người dùng không tồn tại");
    }
    return userAccount;
  }

  public List<UserAccountResponse> getAllUsers(){
    return modelMapper.toUserAccountResponse(userStorage.findAll());
  }

  public UserAccountResponse getUserAccountProfile(Integer userId) {
    UserAccount userAccount = userStorage.findById(Long.parseLong(String.valueOf(userId)));
    return modelMapper.toUserAccountResponse(userAccount);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
  public UserAccountResponse updateProfile(Integer userId, UpdateProfileDto updateProfileDto) {
    UserAccount userAccount = userStorage.findById(Long.parseLong(String.valueOf(userId)));
    userAccount.setFullName(updateProfileDto.getFullName());
    if(userAccount.getEmail() == null || !userAccount.getEmail().equals(updateProfileDto.getEmail())) {
      boolean isExistEmail = userStorage.existByEmail(updateProfileDto.getEmail());
      if (isExistEmail) {
        throw new ResourceFoundException("Email: " + updateProfileDto.getEmail() + " đã tồn tại");
      }
    }
    userAccount.setEmail(updateProfileDto.getEmail());
    if(userAccount.getPhoneNumber() == null || !userAccount.getPhoneNumber().equals(updateProfileDto.getPhoneNumber())) {

    }
    userAccount.setPhoneNumber(updateProfileDto.getPhoneNumber());
    userAccount.setUpdatedAt(System.currentTimeMillis());
    userStorage.save(userAccount);
    return modelMapper.toUserAccountResponse(userAccount);
  }

  public Boolean changePassword(UserDetails userDetails, ChangePasswordDto dto) {
    UserAccount userAccount = userStorage.findByUsername(userDetails.getUsername());
    if (!encoder.matches(dto.getOldPassword(), userAccount.getPassword())) {
      // Nếu mật khẩu cũ không khớp, ném ngoại lệ
      throw new BadRequestException("Mật khẩu cũ không đúng");
    }
    if(!dto.getNewPassword().equals(dto.getConfirmPassword())) {
      throw new BadRequestException("Mật khẩu không khớp");
    }
    userAccount.setPassword(encoder.encode(dto.getNewPassword()));
    userAccount.setUpdatedAt(System.currentTimeMillis());
    userStorage.save(userAccount);
    return true;
  }
}
