package com.project4.restaurant.domain.core.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Service
public class LocalizationService {
  private final MessageSource messageSource;

  private final LocaleResolver localeResolver;

  private final HttpServletRequest request;

  public LocalizationService(MessageSource messageSource, LocaleResolver localeResolver, HttpServletRequest request) {
    this.messageSource = messageSource;
    this.localeResolver = localeResolver;
    this.request = request;
  }

  public String getMessage(String key) {
    Locale locale = localeResolver.resolveLocale(request);
    return messageSource.getMessage(key, null, key, locale);
  }

  public String getMessage(String key, Object[] args) {
    Locale locale = localeResolver.resolveLocale(request);
    return messageSource.getMessage(key, args, key, locale);
  }

}