package com.project4.restaurant.app.dto.notification;

import com.project4.restaurant.domain.Constants;
import com.project4.restaurant.domain.util.Helper;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class SendNotificationDto {
  private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

  private Integer id;
  private Long dateNotify;
  private String title;
  private String message;
  private String thumb;

  public SendNotificationDto(String content) {
    id = ID_GENERATOR.incrementAndGet();
    dateNotify = Helper.getNowMillisAtUtc();
    title = Constants.NOTIFICATION_TITLE;
    message = content;
  }
}