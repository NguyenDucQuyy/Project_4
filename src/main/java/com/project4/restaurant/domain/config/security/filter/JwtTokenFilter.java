package com.project4.restaurant.domain.config.security.filter;

import com.project4.restaurant.domain.core.exception.AccountDeletedException;
import com.project4.restaurant.domain.entity.type.AccountState;
import com.project4.restaurant.domain.entity.user.UserAccount;
import com.project4.restaurant.domain.pojo.PayloadInfo;
import com.project4.restaurant.domain.provider.JwtTokenProvider;
import com.project4.restaurant.domain.service.UserAccountService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Log4j2
public class JwtTokenFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;

  @Autowired
  @Lazy
  private UserAccountService userService;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = jwtTokenProvider.resolveToken(request);
    try {
      if (token != null && jwtTokenProvider.validateToken(token)) {

        PayloadInfo payloadInfo = jwtTokenProvider.getPayloadInfo(token);

        UserDetails userDetails = userService.loadUserByUsername(payloadInfo.getUsername());
        UserAccount user = (UserAccount) userDetails;
        if (user.getState() == AccountState.DELETED) {
          throw new AccountDeletedException("Tài khoản đã bị xóa");
        }
        TokenInfo authentication = new TokenInfo(userDetails, null, userDetails.getAuthorities());
        authentication.setUserId(payloadInfo.getUserId());
        authentication.setUsername(payloadInfo.getUsername());
        authentication.setFullName(user.getFullName());
        authentication.setRole(user.getRole());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (JwtException | IllegalArgumentException e) {
      // Invalid JWT token
      SecurityContextHolder.clearContext();
    } catch (AccountDeletedException ex) {
      throw new AccountDeletedException(ex.getMessage());
    }
    filterChain.doFilter(request, response);
  }
}
