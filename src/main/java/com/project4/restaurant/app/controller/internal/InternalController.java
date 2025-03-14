//package com.project4.restaurant.app.controller.internal;
//
//import com.project4.restaurant.domain.service.notification.NotificationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("it/project_4/restaurant")
//public class InternalController {
//  @Autowired
//  private StatisticService statisticService;
//
//  @Autowired
//  private TaskService taskService;
//
//  @Autowired
//  private NotificationService notificationService;
//
//  @PostMapping("/update-task-process")
//  public ResponseEntity<Boolean> updateTaskProcessPerDay(){
//    taskService.updateTaskProcessPerDay();
//    return ResponseEntity.ok(true);
//  }
//
//  @PostMapping("/statistic/today")
//  public ResponseEntity<Boolean> updateStatisticToDay(){
//    statisticService.updateStatisticToDay();
//    return ResponseEntity.ok(true);
//  }
//
//  @PostMapping("/statistic/pre-day")
//  public ResponseEntity<Boolean> updateStatisticPreDay(){
//    statisticService.updateStatisticPreDay();
//    return ResponseEntity.ok(true);
//  }
//
//  @PostMapping("/send-mail")
//  public ResponseEntity<Boolean> sendTaskNotification(){
//    notificationService.processSendEmail();
//    return ResponseEntity.ok(true);
//  }
//
//  //@CrossOrigin(origins = "http://localhost:3000")
//  @PostMapping("/notification")
//  public ResponseEntity<Boolean> notificationTask() {
//    notificationService.processSendNotification();
//    return ResponseEntity.ok(true);
//  }
//}
