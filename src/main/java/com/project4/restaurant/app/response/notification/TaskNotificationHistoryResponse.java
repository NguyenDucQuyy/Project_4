package com.project4.restaurant.app.response.notification;

import com.project4.restaurant.domain.entity.type.ReadNotificationType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskNotificationHistoryResponse {
  private Long id;

  private Long taskId;

  private Long departmentId;

  private String taskName;

  private String taskCode;

  private String notificationContent;

  private Long notificationTime;

  private LocalDate dueDate;

  private ReadNotificationType readNotiType;
}
