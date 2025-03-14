package com.project4.restaurant.domain.core.exception;

import com.project4.restaurant.domain.core.exception.base.BaseException;
import com.project4.restaurant.domain.core.exception.base.ErrorCode;
import com.project4.restaurant.domain.core.exception.base.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.NestedServletException;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler({ResourceFoundException.class})
  public ResponseEntity<Object> handleFoundException(BaseException exception, WebRequest webRequest) {
    return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.FOUND);
  }

  @ExceptionHandler({AccountNotExistsException.class, ResourceNotFoundException.class})
  public ResponseEntity<Object> handleNotFoundException(
      BaseException exception, WebRequest webRequest) {
    return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({EmptyResultDataAccessException.class})
  public ResponseEntity<Object> handleNotFoundException(
      Exception exception, WebRequest webRequest) {
    return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({UnAuthenticationException.class})
  public ResponseEntity<Object> handleUnAuthenticationException(UnAuthenticationException exception, WebRequest webRequest) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(401,"Token không hợp lệ"));
  }

  @ExceptionHandler({SignatureException.class, ExpiredJwtException.class})
  public ResponseEntity<Object> handleForBiddenException(){
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(403,"Refresh token không hợp lệ"));
  }
  @ExceptionHandler({WrongAccountOrPasswordException.class, UnauthorizedException.class, NotAuthorizedException.class})
  public ResponseEntity<Object> handleUnAuthorizedException(
      BaseException exception, WebRequest webRequest) {
    return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({MalformedJwtException.class})
  public ResponseEntity<Object> handleUnAuthorizedException(
      Exception exception, WebRequest webRequest) {
    return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<Object> handleAccessDeniedException(
      AccessDeniedException exception, WebRequest webRequest) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(ErrorCode.ACCESS_DENIED, "Bạn không có quyền truy cập"));
  }

  @ExceptionHandler({
      NestedServletException.class,
      IllegalStateException.class,
      IllegalStateException.class
  })
  public ResponseEntity<?> handleAllException(Exception exception, WebRequest webRequest) {
    return new ResponseEntity<>(
        ExceptionResponse.createFrom(exception), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({AccessError.class, BadRequestException.class, RequestFailedException.class, NotEnoughException.class, })
  public ResponseEntity<?> handleBadRequestException(Exception exception, WebRequest webRequest) {
    return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
    log.error("=======globalExceptionHandler: ", ex);
    return new ResponseEntity<>(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Có lỗi xảy ra!"), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({UnauthorizedAccessException.class})
  public ResponseEntity<?> handleUnauthorizedAccessException(Exception exception, WebRequest webRequest) {
    return new ResponseEntity<>(ExceptionResponse.createFrom(exception), HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    return new ResponseEntity<>(ExceptionResponse.createFrom(new BaseException(ErrorCode.NOT_VALID, Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage())), HttpStatus.BAD_REQUEST);
  }
}