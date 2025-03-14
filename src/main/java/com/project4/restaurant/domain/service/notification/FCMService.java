//package com.project4.restaurant.domain.service.notification;
//
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingException;
//import com.google.firebase.messaging.Message;
//import com.google.firebase.messaging.Notification;
//import com.project4.restaurant.app.dto.notification.SendNotificationDto;
//import com.project4.restaurant.domain.entity.task.TaskNotificationHistory;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Service;
//
//@Service
//@Log4j2
//public class FCMService {
//  public void sendPushNotificationService(SendNotificationDto notification
//      , UserTokenDevice userTokenDevice, TaskNotificationHistory taskNotificationHistory) {
//    try {
//      Notification firebaseNotification = Notification.builder()
//          .setTitle(notification.getTitle())
//          .setBody(notification.getMessage())
//          .setImage(notification.getThumb())
//          .build();
//
//      log.debug("=========sendPushNotificationService: UserToken, PhoneNumber {}, {}"
//          , userTokenDevice.getUserDeviceToken());
//      Message message = Message.builder()
//          .setNotification(firebaseNotification)
//          .setToken(userTokenDevice.getUserDeviceToken())
//          .putData("dateNotify", notification.getDateNotify().toString())
//          .putData("taskProcess", taskNotificationHistory.getTaskProcess().name())
//          .putData("taskName", taskNotificationHistory.getTaskName())
//          .putData("taskCode", taskNotificationHistory.getTaskCode())
//          .putData("dueDate", taskNotificationHistory.getDueDate().toString())
//          .putData("id", taskNotificationHistory.getId().toString())
//          .build();
//
//      String response = FirebaseMessaging.getInstance().send(message);
//      log.info("=======sendPushNotificationService: {}", response);
//    } catch (FirebaseMessagingException e) {
//      log.error("=======sendPushNotificationService error: {}", e.getMessage());
//    }
//  }
//}