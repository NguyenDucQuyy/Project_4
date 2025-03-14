package com.project4.restaurant.app.response.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public class TaskNotificationResponse {
  private List<?> data;
  private NotificationMetadata metadata;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class NotificationMetadata {
    private long total;
    private long totalPages;

    public static NotificationMetadata createFrom(Page<?> page){
      return new NotificationMetadata(
          page.getTotalElements(),
          page.getTotalPages());
    }
  }

  public TaskNotificationResponse(Page<TaskNotificationHistoryResponse> page) {
    this.data = page.getContent();
    this.metadata = new NotificationMetadata(page.getTotalElements(), page.getTotalPages());
  }
}
