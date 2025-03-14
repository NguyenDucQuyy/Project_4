package com.project4.restaurant.domain.config.security.filter;

import com.project4.restaurant.domain.util.Helper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Log4j2
public class LoggingFilter extends OncePerRequestFilter {
  @Value("${server.timeoutSlowApi}")
  private Integer timeoutSlowApi;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    long start = Helper.getNowMillisAtUtc();

    filterChain.doFilter(request, response);

    long duration = Helper.getNowMillisAtUtc() - start;
    MDC.put("duration", duration + "");
    if (duration > timeoutSlowApi) {
      log.warn("end request ==> {}  {}", (request).getMethod(), Helper.getPath(request));
    } else {
      log.debug("end request ==> {}  {}", (request).getMethod(), Helper.getPath(request));
    }
    MDC.remove("duration");
  }
}
