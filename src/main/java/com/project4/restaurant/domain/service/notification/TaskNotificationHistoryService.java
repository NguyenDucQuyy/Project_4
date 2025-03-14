//package com.project4.restaurant.domain.service.notification;
//
//import com.project4.restaurant.app.response.notification.TaskNotificationHistoryResponse;
//import com.project4.restaurant.app.response.notification.TaskNotificationResponse;
//import com.project4.restaurant.domain.Constants;
//import com.project4.restaurant.domain.entity.task.TaskNotificationHistory;
//import com.project4.restaurant.domain.entity.type.ReadNotificationType;
//import com.project4.restaurant.domain.service.base.BaseService;
//import com.project4.restaurant.domain.util.Helper;
//import lombok.extern.log4j.Log4j2;
//import org.redisson.api.RAtomicLong;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//@Log4j2
//public class TaskNotificationHistoryService extends BaseService {
//  public TaskNotificationResponse getNotificationHistories(String username, Pageable pageable) {
//    LocalDateTime now = Helper.getNowDateTimeAtVn();
//    Long start = Helper.convertLocalDateTimeToLong(now.minusDays(30));
//    Long end = Helper.convertLocalDateTimeToLong(now);
//    Page<TaskNotificationHistory> taskNotificationHistories = taskNotificationHistoryStorage
//        .findAllByUsernameAndNotificationTimeBetweenOrderByNotificationTimeDesc(username, start, end, pageable);
//    Page<TaskNotificationHistoryResponse> pageHistories =
//        taskNotificationHistories.map(history -> modelMapper.toTaskNotificationHistoryResponse(history));
//    RAtomicLong notReadQuantityCache = redissonClient.getAtomicLong(cacheKey.genUserNotificationCount(username));
//    notReadQuantityCache.delete();
//    return new TaskNotificationResponse(pageHistories);
//  }
//
//  public Long getUnreadNotificationCount(String username) {
//    RAtomicLong notReadQuantityCache = redissonClient.getAtomicLong(cacheKey.genUserNotificationCount(username));
//    return notReadQuantityCache.get();
//  }
//
//  public boolean updateReadNotificationStatus(Long id, Long notificationTime) {
//    if (id == null) {
//      return false;
//    }
//    try {
//      TaskNotificationHistory notification = taskNotificationHistoryStorage.findTaskNotificationHistoryByIdAndNotificationTime(id, notificationTime);
//      if (notification == null) {
//        log.error("updateReadNotiStatus NotificationNotFound with id: {}", id);
//        return false;
//      }
//
//      if (ReadNotificationType.READ.equals(notification.getReadNotiType())) {
//        log.debug("updateReadNotiStatus NotificationAlreadyRead with id: {}", id);
//        return false;
//      }
//
//      notification.setReadNotiType(ReadNotificationType.READ);
//      taskNotificationHistoryStorage.save(notification);
//      luaCacheService.evictByPatternBatch(Constants.TASK_NOTI_HISTORY_CACHE
//          , cacheKey.genPageFilterForEvict(notification.getUsername()));
//      return true;
//    } catch (Exception e) {
//      log.error("updateReadNotiStatusError with id: {}", id, e);
//      return false;
//    }
//  }
//}
