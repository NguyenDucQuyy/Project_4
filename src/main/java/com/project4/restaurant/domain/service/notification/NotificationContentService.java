package com.project4.restaurant.domain.service.notification;

import com.project4.restaurant.app.response.notification.TaskNotificationWarningResponse;
import com.project4.restaurant.domain.Constants;
import com.project4.restaurant.domain.storage.common.ConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationContentService {
  @Autowired
  private ConfigStorage configStorage;

  public String generateNotificationContent(TaskNotificationWarningResponse info) {
    return switch (info.getTaskProcess()){
      case "NOT_DUE", "ON_TIME" -> null;
      case "NEARLY_DUE" -> String.format(
          configStorage.getValueConfigByKeyConfigCache(Constants.TASK_NEARLY_DUE_MESSAGE),
          info.getTaskName(),
          info.getTaskCode(),
          info.getDueDate()
      );
      case "OVER_DUE" -> String.format(
          configStorage.getValueConfigByKeyConfigCache(Constants.TASK_OVER_DUE_MESSAGE),
          info.getTaskName(),
          info.getTaskCode(),
          info.getDueDate()
      );
      default -> throw new IllegalStateException("Unexpected value: " + info.getTaskProcess());
    };
  }
}
