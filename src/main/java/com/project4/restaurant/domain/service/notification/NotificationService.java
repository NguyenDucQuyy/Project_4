//package com.project4.restaurant.domain.service.notification;
//
//import com.project4.restaurant.app.dto.notification.SendNotificationDto;
//import com.project4.restaurant.app.response.notification.TaskNotificationWarningResponse;
//import com.project4.restaurant.domain.Constants;
//import com.project4.restaurant.domain.entity.department.Department;
//import com.project4.restaurant.domain.entity.task.TaskNotificationHistory;
//import com.project4.restaurant.domain.entity.type.AccountState;
//import com.project4.restaurant.domain.entity.type.ReadNotificationType;
//import com.project4.restaurant.domain.entity.user.UserAccount;
//import com.project4.restaurant.domain.service.base.BaseService;
//import com.project4.restaurant.domain.util.Helper;
//import com.project4.restaurant.domain.util.JsonParser;
//import jakarta.mail.*;
//import jakarta.mail.internet.InternetAddress;
//import jakarta.mail.internet.MimeMessage;
//import lombok.extern.log4j.Log4j2;
//import net.sargue.mailgun.Configuration;
//import net.sargue.mailgun.Mail;
//import net.sargue.mailgun.Response;
//import org.redisson.api.RAtomicLong;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.sql.Date;
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.Executors;
//
//@Service
//@Log4j2
//public class NotificationService extends BaseService {
//  @Autowired
//  private NotificationContentService notificationContentService;
//  @Autowired
//  private FCMService fcmService;
//  @Lazy
//  @Autowired
//  private NotificationService self;
//  @Autowired
//  private RestTemplate restTemplate;
//  public static final String MAILGUN = "MAILGUN";
//  @Value("${mail.mail-server:MAILGUN}")
//  private String mailServer;
//  @Value("${mail.mailgun.api-key}")
//  private String mailgunApiKey;
//  @Value("${mail.mailgun.domain-name}")
//  private String mailgunDomainName;
//  @Value("${mail.mailgun.from}")
//  private String mailgunFrom;
//  @Value("${mail.smtp.user}")
//  private String mailSmtp;
//  @Value("${mail.smtp.pass}")
//  private String mailPass;
//  @Value("${mail.smtp.domain-name}")
//  private String mailDomainName;
//  @Value("${mail.smtp.port}")
//  private String mailPort;
//  @Value("${mail.smtp.host}")
//  private String mailHost;
//
//  private final LocalDate today = Helper.getNowDateAtVn();
//  private final LocalDate firstDayOfMonth = today.withDayOfMonth(1);
//  private final LocalDate lastDayOfMonth = YearMonth.from(today).atEndOfMonth();
//
////  public void processSendNotification(TaskNotificationInfo taskNotificationDto) {
////    if(taskNotificationDto.getTaskProcess() == null) {
////      return;
////    }
////    log.debug("=========>processSendNotification: taskNotificationDto{}", taskNotificationDto);
////    try{
////      String content = notificationContentService.generateNotificationContent(taskNotificationDto);
////      List<UserTokenDevice> tokenDevices = userTokenDeviceRepository.findByUserId(taskNotificationDto.getUserId());
////      SendNotificationDto sendNotificationDto = new SendNotificationDto(content);
////      TaskNotificationHistory notificationHistory = new TaskNotificationHistory();
////      notificationHistory.setTaskId(taskNotificationDto.getTaskId());
////      notificationHistory.setTaskCode(taskNotificationDto.getTaskCode());
////      notificationHistory.setTaskName(taskNotificationDto.getTaskName());
////      notificationHistory.setTaskProcess(taskNotificationDto.getTaskProcess());
////      notificationHistory.setNotificationContent(sendNotificationDto.getMessage());
////      notificationHistory.setDueDate(Helper.convertStringToLocalDate(taskNotificationDto.getDueDate(), "dd/MM/yyyy"));
////      notificationHistory.setReadNotiType(ReadNotificationType.NOT_READ);
////      notificationHistory.setNotificationTime(Helper.getNowMillisAtUtc());
////      TaskNotificationHistory taskNotificationHistory = self.saveTaskNotificationHistory(notificationHistory);
////      luaCacheService.evictByPatternBatch(
////          Constants.TASK_NOTI_HISTORY_CACHE, cacheKey.genPageFilterForEvict(taskNotificationDto.getUserId()));
////      RAtomicLong notificationCounter = redissonClient.getAtomicLong(cacheKey.genUserNotificationCount(taskNotificationDto.getUserId().toString()));
////      notificationCounter.incrementAndGet();
////
////      for (UserTokenDevice tokenDevice : tokenDevices) {
////        fcmService.sendPushNotificationService(sendNotificationDto, tokenDevice, taskNotificationHistory);
////      }
////    }catch (Exception e){
////      log.error("==============>processSendNotification exception = {}", e.getMessage());
////    }
////  }
//
//  public TaskNotificationHistory saveTaskNotificationHistory(TaskNotificationHistory taskNotificationHistory) {
//    return taskNotificationHistoryRepository.save(taskNotificationHistory);
//  }
//
//  public void processSendTelegramTelegram(Integer userId) {
//    UserAccount user = userStorage.findById(Long.parseLong(String.valueOf(userId)));
//    Set<Department> userDepartments = user.getDepartments();
//    List<Integer> departmentIds = userDepartments.stream().map(Department::getId).toList();
//    String taskTelegramToken = "7739886207:AAHlw8NDdCLm9INTgZecrE_dAChDx8EgNFc";
//    String taskTelegramChatId = "-4636237134";
//    try{
//      List<Map<String, Object>> rawResults = taskStorage.findTaskNotificationByDepartments(departmentIds);
//      List<TaskNotificationWarningResponse> tasksNotifications = rawResults.stream()
//          .map(row -> {
//            Long taskId = (Long) row.get("taskId");
//            Integer departmentId = (Integer) row.get("departmentId");
//            String taskCode = (String) row.get("taskCode");
//            String taskName = (String) row.get("taskName");
//            String taskProcess = (String) row.get("taskProcess");
//            String taskState = (String) row.get("taskState");
//            Object dueDateObj = row.get("dueDate");
//            LocalDate dueDate = dueDateObj != null ? ((Date) dueDateObj).toLocalDate() : null;
//            assert dueDate != null;
//            String formattedDueDate = dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//            return new TaskNotificationWarningResponse(taskId, departmentId, taskCode, taskName, taskProcess, taskState, formattedDueDate);
//          }).toList();
//      telegramNotification(tasksNotifications, taskTelegramToken, taskTelegramChatId);
//    }
//    catch (Exception e) {
//      log.error("===============> processSendTelegram: ) {}", e.getMessage());
//    }
//  }
//
//  private void telegramNotification(List<TaskNotificationWarningResponse> taskNotificationResponses, String taskTelegramToken, String taskTelegramChatId){
//    StringBuilder messageBuilder = new StringBuilder();
//    messageBuilder.append("==================Danh sách nhiệm vụ==================\n");
//    int i = 1;
//    for (TaskNotificationWarningResponse response : taskNotificationResponses) {
//      String statusMessage = "";
//      if (response.getTaskProcess().equals("NEARLY_DUE")) {
//        statusMessage = "sắp đến hạn xử lý: ";
//      } else if (response.getTaskProcess().equals("OVER_DUE")) {
//        statusMessage = "quá hạn xử lý: ";
//      }
//      messageBuilder
//          .append(i).append(".")
//          .append(response.getTaskName()).append("[")
//          .append(response.getTaskCode()).append("] ")
//          .append(statusMessage)
//          .append(response.getDueDate()).append("\n");
//      i++;
//    }
//    sendTelegram(taskTelegramToken, taskTelegramChatId, messageBuilder.toString());
//  }
//
//  private void sendTelegram(String botToken, String chatId, String message) {
//    String urlString = "https://api.telegram.org/bot%s/sendMessage";
//    try {
//      urlString = String.format(urlString, botToken);
//      Map<String, Object> data = new HashMap<>();
//      data.put("chat_id", chatId);
//      data.put("parse_mode", "HTML");
//      data.put("text", message);
//      // Set headers
//      HttpHeaders headers = new HttpHeaders();
//      headers.setContentType(MediaType.APPLICATION_JSON);
//      HttpEntity<String> entity = new HttpEntity<>(JsonParser.toJson(data), headers);
//      restTemplate.postForEntity(urlString, entity, String.class);
//    } catch (Exception e) {
//      log.error("Error: {}", e.getMessage(), e);
//    }
//  }
//
//  //send mail
//  public void processSendEmail() {
//    List<UserAccount> users = userStorage.findAllUserByState(AccountState.ACTIVE);
//    users.stream().forEach(user -> {
//      Set<Department> userDepartments = user.getDepartments();
//      //List<Integer> departmentIds = userDepartments.stream().map(Department::getId).toList();
//      for(Department department : userDepartments) {
//        String subject = "[Cảnh báo]" + "Nhiệm vụ " + department.getDepartmentName();
//        try{
//          List<Map<String, Object>> rawResults = taskStorage.findTaskNotificationByDepartments(List.of(department.getId()));
//          if(rawResults.isEmpty()){
//            log.info("User {} không có nhiệm vụ trong phòng ban {}", user.getUsername(), department.getDepartmentName());
//            return;
//          }
//          List<TaskNotificationWarningResponse> tasksNotifications = rawResults.stream()
//              .filter(row -> {
//                String taskProcess = (String) row.get("taskProcess");
//                String taskState = (String) row.get("taskState");
//                Object dueDateObj = row.get("dueDate");
//                LocalDate dueDate = dueDateObj != null ? ((Date) dueDateObj).toLocalDate() : null;
//                assert dueDate != null;
//                return !dueDate.isBefore(firstDayOfMonth) && !dueDate.isAfter(lastDayOfMonth)
//                    &&(taskProcess.equals(TaskProcess.NEARLY_DUE.name()) || taskProcess.equals(TaskProcess.OVER_DUE.name()))
//                    && (taskState.equals(TaskState.NOT_STARTED.name()) || taskState.equals(TaskState.IN_PROCESS.name()));
//              })
//              .map(row -> {
//                Long taskId = (Long) row.get("taskId");
//                Integer departmentId = (Integer) row.get("departmentId");
//                String taskCode = (String) row.get("taskCode");
//                String taskName = (String) row.get("taskName");
//                String taskProcess = (String) row.get("taskProcess");
//                String taskState = (String) row.get("taskState");
//                Object dueDateObj = row.get("dueDate");
//                LocalDate dueDate = dueDateObj != null ? ((Date) dueDateObj).toLocalDate() : null;
//                assert dueDate != null;
//                String formattedDueDate = dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//                return new TaskNotificationWarningResponse(taskId, departmentId, taskCode, taskName, taskProcess, taskState, formattedDueDate);
//              }).toList();
//          if (!tasksNotifications.isEmpty()) {
//            mailNotification(user, department, tasksNotifications, subject);
//          }
//        }
//        catch (Exception e) {
//          log.error("===============> processSendEmail: ) {}", e.getMessage());
//        }
//      }
//    });
//  }
//
//  private void mailNotification(UserAccount user, Department department, List<TaskNotificationWarningResponse> tasksNotifications, String subject) {
//    StringBuilder emailBody = new StringBuilder();
//    List<TaskNotificationWarningResponse> nearlyDueTasks = new ArrayList<>();
//    List<TaskNotificationWarningResponse> overDueTasks = new ArrayList<>();
//
//    emailBody.append("<html><body>");
//    emailBody.append("<p>Xin chào, <strong>").append(department.getDepartmentName()).append("</strong> có các nhiệm vụ sau đây cần chú ý:</p>");
//    //emailBody.append("<p>Bạn có các nhiệm vụ sau đây cần chú ý:</p>");
//    emailBody.append("<ul>");
//    int index = 1; // Đánh số thứ tự nhiệm vụ
//    for (TaskNotificationWarningResponse taskNotification : tasksNotifications) {
//      if (taskNotification.getTaskProcess().equals(TaskProcess.NEARLY_DUE.name())) {
//        nearlyDueTasks.add(taskNotification);
//      } else if (taskNotification.getTaskProcess().equals(TaskProcess.OVER_DUE.name())) {
//        overDueTasks.add(taskNotification);
//      }
//    }
//    if (!nearlyDueTasks.isEmpty()) {
//      emailBody.append("<h3>🔶 Nhiệm vụ sắp đến hạn</h3><ul>");
//      for (TaskNotificationWarningResponse task : nearlyDueTasks) {
//        emailBody.append(formatTaskHtml(index++, task));
//      }
//      emailBody.append("</ul>");
//    }
//    if (!overDueTasks.isEmpty()) {
//      emailBody.append("<h3>🔴 Nhiệm vụ quá hạn</h3><ul>");
//      for (TaskNotificationWarningResponse task : overDueTasks) {
//        emailBody.append(formatTaskHtml(index++, task));
//      }
//      emailBody.append("</ul>");
//    }
//    emailBody.append("<p>Trân trọng,<br>VNTA</p>");
//    emailBody.append("</body></html>");
//
//    if (mailServer.equals(MAILGUN)) {
//      //sendMail(mailgunApiKey, mailgunDomainName, mailgunFrom, mailgunDomainName, user.getEmail(), subject, emailBody.toString());
//      sendEmailSmtp(mailHost, mailPort, mailSmtp, mailPass, mailSmtp, mailDomainName, user.getEmail(), subject, emailBody.toString());
//    }
//  }
//
//  private String formatTaskHtml(int index, TaskNotificationWarningResponse task) {
//    return "<li><strong>" + index + ". " + task.getTaskName() + "</strong><br>"
//        + "&nbsp;&nbsp;&nbsp;📌 <strong>Số ký hiệu:</strong> " + task.getTaskCode() + "<br>"
//        //+ "&nbsp;&nbsp;&nbsp;⏳ <strong>Trạng thái:</strong> " + task.getTaskProcess() + "<br>"
//        + "&nbsp;&nbsp;&nbsp;🗓 <strong>Hạn xử lý:</strong> " + task.getDueDate() + "</li><br>";
//  }
//
//  private void sendMail(String apiKey, String domainName, String from, String fromName, String to, String subject, String body) {
//    try {
//      Configuration configuration = new Configuration()
//          .domain(domainName)
//          .apiUrl("https://api.mailgun.net/v3")
//          .apiKey(apiKey)
//          .from(fromName, from);
//      Response response = Mail.using(configuration)
//          .to(to)
//          .subject(subject + "_" +Helper.localDateToString(Helper.getNowDateAtVn(), "dd/MM/yyyy"))
//          .html(body)
//          .build()
//          .send();
//      log.debug("Response from server: {}", response.responseMessage());
//    } catch (Exception e) {
//      log.error("Error: {}", e.getMessage());
//    }
//  }
//
//  //Send mail Smtp
//  private void sendEmailSmtp(String smtpHost, String smtpPort, String emailSmtp, String password, String fromEmail, String fromName, String to, String subject, String body) {
//    try{
//      Properties properties = new Properties();
//      properties.put("mail.smtp.auth", "true");
//      properties.put("mail.smtp.starttls.enable", "true");
//      properties.put("mail.smtp.starttls.required", "true");
//      properties.put("mail.smtp.host", smtpHost);
//      properties.put("mail.smtp.port", smtpPort);
//      properties.put("mail.smtp.ssl.trust", smtpHost);
//
//      Session session = Session.getInstance(properties, new Authenticator() {
//        @Override
//        protected PasswordAuthentication getPasswordAuthentication() {
//          return new PasswordAuthentication(emailSmtp, password);
//        }
//      });
//
//      Message message = new MimeMessage(session);
//      message.setFrom(new InternetAddress(fromEmail, fromName, "UTF-8"));
//      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//      message.setSubject(subject);
//      message.setContent(body, "text/html; charset=utf-8");
//
//      Transport.send(message);
//      log.debug("===============Email sent successfully: {}", message);
//    } catch (Exception e) {
//      log.error("===============Error sending email: " + e.getMessage());
//    }
//  }
//
//  public void processSendNotification() {
//    List<UserAccount> users = userStorage.findAllUserByState(AccountState.ACTIVE);
//    users.stream().forEach(user -> {
//      List<UserTokenDevice> tokenDevices = userTokenDeviceRepository.findByUserId(user.getId());
//      if (tokenDevices == null || tokenDevices.isEmpty()) {
//        log.error("Không tìm thấy UserTokenDevice nào cho user: {}", user.getUsername());
//        return;
//      }
//      Set<Department> userDepartments = user.getDepartments();
//      List<Integer> departmentIds = userDepartments.stream().map(Department::getId).toList();
//      if (departmentIds.isEmpty()) {
//        log.error("Không tìm thấy Phòng ban nào cho user: {}", user.getUsername());
//        return;
//      }
//      try{
//        List<Map<String, Object>> rawResults = taskStorage.findTaskNotificationByDepartments(departmentIds);
//        if(rawResults.isEmpty()){
//          log.info("User {} không có nhiệm vụ trong phòng ban của mình", user.getUsername());
//          return;
//        }
//        List<TaskNotificationWarningResponse> tasksNotifications = rawResults.stream()
//            .filter(row -> {
//              String taskProcess = (String) row.get("taskProcess");
//              String taskState = (String) row.get("taskState");
//              Object dueDateObj = row.get("dueDate");
//              LocalDate dueDate = dueDateObj != null ? ((Date) dueDateObj).toLocalDate() : null;
//              assert dueDate != null;
//              return !dueDate.isBefore(firstDayOfMonth) && !dueDate.isAfter(lastDayOfMonth)
//                  &&(taskProcess.equals(TaskProcess.NEARLY_DUE.name()) || taskProcess.equals(TaskProcess.OVER_DUE.name()))
//                  && (taskState.equals(TaskState.NOT_STARTED.name()) || taskState.equals(TaskState.IN_PROCESS.name()));
//            })
//            .map(row -> {
//              Long taskId = (Long) row.get("taskId");
//              Integer departmentId = (Integer) row.get("departmentId");
//              String taskCode = (String) row.get("taskCode");
//              String taskName = (String) row.get("taskName");
//              String taskProcess = (String) row.get("taskProcess");
//              String taskState = (String) row.get("taskState");
//              Object dueDateObj = row.get("dueDate");
//              LocalDate dueDate = dueDateObj != null ? ((Date) dueDateObj).toLocalDate() : null;
//              assert dueDate != null;
//              String formattedDueDate = dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//              return new TaskNotificationWarningResponse(taskId, departmentId, taskCode, taskName, taskProcess, taskState, formattedDueDate);
//            }).toList();
//        for(TaskNotificationWarningResponse taskNotification : tasksNotifications){
////          boolean exists = taskNotificationHistoryStorage.existsTaskNotificationByTaskIdAndDepartmentIdAndUsername(taskNotification.getTaskId(), taskNotification.getDepartmentId(), user.getUsername());
////          if(exists){
////            log.info("Thông báo cho nhiệm vụ {} đã tồn tại, bỏ qua...", taskNotification.getTaskId());
////            continue;
////          }vanthu4
//          String content = notificationContentService.generateNotificationContent(taskNotification);
//          if (content == null) {
//            log.error("Generated notification content is null for task: {}", taskNotification.getTaskId());
//            continue;
//          }
//          SendNotificationDto sendNotificationDto = new SendNotificationDto(content);
//          String message = sendNotificationDto.getMessage();
//          if (message == null) {
//            log.error("Notification message is null for task: {}", taskNotification.getTaskId());
//            continue;
//          }
//          TaskNotificationHistory notificationHistory = new TaskNotificationHistory();
//          notificationHistory.setTaskId(taskNotification.getTaskId());
//          notificationHistory.setDepartmentId(taskNotification.getDepartmentId());
//          notificationHistory.setTaskCode(taskNotification.getTaskCode());
//          notificationHistory.setTaskName(taskNotification.getTaskName());
//          notificationHistory.setUsername(user.getUsername());
//          notificationHistory.setTaskProcess(TaskProcess.valueOf(taskNotification.getTaskProcess()));
//          notificationHistory.setTaskState(TaskState.valueOf(taskNotification.getTaskState()));
//          notificationHistory.setNotificationContent(message);
//          notificationHistory.setDueDate(Helper.convertStringToLocalDate(taskNotification.getDueDate(), "dd/MM/yyyy"));
//          notificationHistory.setReadNotiType(ReadNotificationType.NOT_READ);
//          notificationHistory.setNotificationTime(Helper.getNowMillisAtUtc());
//          //synchronized (this) { //Đảm bảo lưu lịch sử thông báo không bị conflict
//            TaskNotificationHistory taskNotificationHistory = self.saveTaskNotificationHistory(notificationHistory);
//            luaCacheService.evictByPatternBatch(
//                Constants.TASK_NOTI_HISTORY_CACHE, cacheKey.genPageFilterForEvict(user.getId()));
//            RAtomicLong notificationCounter = redissonClient.getAtomicLong(cacheKey.genUserNotificationCount(user.getUsername()));
//            notificationCounter.incrementAndGet();
//            //Gửi thông báo đến tất cả device cùng 1 lúc
//            List<CompletableFuture<Void>> futures = tokenDevices.stream()
//                .map(tokenDevice -> CompletableFuture.runAsync(() -> {
//                  log.info("===============> Sending notification to device: {}", tokenDevice.getUserDeviceToken());
//                  fcmService.sendPushNotificationService(sendNotificationDto, tokenDevice, taskNotificationHistory);
//                }, Executors.newCachedThreadPool())).toList();
//            CompletableFuture.allOf(futures.toArray(futures.toArray(new CompletableFuture[0]))).join();
//          //}
//        }
//      }
//      catch (Exception e) {
//        log.error("===============> processSendNotification Error: {}", e.getMessage());
//      }
//    });
//  }
//}