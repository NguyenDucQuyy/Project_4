//package com.project4.restaurant.app.controller.admin;
//
//import com.project4.restaurant.domain.service.notification.NotificationService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping(path = "/v1/project_4/admin/mail")
//@Tag(name = "Mail Smtp")
//public class SendEmailSmtpController {
//  @Autowired
//  private NotificationService notificationService;
//
//  @PostMapping("/send-mail")
//  public ResponseEntity<Boolean> sendTaskNotification(){
//    notificationService.processSendEmail();
//    return ResponseEntity.ok(true);
//  }
//}
