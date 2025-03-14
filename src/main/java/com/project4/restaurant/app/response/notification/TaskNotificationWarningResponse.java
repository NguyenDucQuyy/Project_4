package com.project4.restaurant.app.response.notification;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NotEmpty
public class TaskNotificationWarningResponse {
  private Long taskId;
  private Integer departmentId;
  private String taskCode;
  private String taskName;
  private String taskProcess;
  private String taskState;
  private String dueDate;
}