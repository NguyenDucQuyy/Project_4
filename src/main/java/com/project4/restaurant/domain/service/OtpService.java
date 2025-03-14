package com.project4.restaurant.domain.service;

import com.project4.restaurant.domain.pojo.OtpInfo;
import com.project4.restaurant.domain.storage.common.ConfigStorage;
import com.project4.restaurant.domain.util.JsonParser;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import net.sargue.mailgun.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@Log4j2
public class OtpService {

  public static final String MAILGUN = "MAILGUN";
  public static final String SMTP = "SMTP";

  @Value("${mail.mail-server:MAILGUN}")
  private String mailServer;

  @Value("${mail.mailgun.api-key}")
  private String mailgunApiKey;

  @Value("${mail.mailgun.domain-name}")
  private String mailgunDomainName;

  @Value("${mail.mailgun.from}")
  private String mailgunFrom;

  @Value("${bot.token}")
  private String telegramToken;

  @Value("${bot.chatId}")
  private String telegramChatId;

  @Value("${mail.smtp.user}")
  private String mailSmtp;

  @Value("${mail.smtp.pass}")
  private String mailPass;

  @Value("${mail.smtp.domain-name}")
  private String mailDomainName;

  @Value("${mail.smtp.port}")
  private String mailPort;

  @Value("${mail.smtp.host}")
  private String mailHost;


  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ConfigStorage configStorage;

  public void processOtp(OtpInfo otpInfo) {
    String body = "";
    if (otpInfo.getMethod().equals(OtpInfo.Method.EMAIL)) {
      switch (otpInfo.getType()) {
        case OtpInfo.Type.NEW_ACCOUNT:
          body = this.genBodySendNotification(otpInfo.getOtpCode());
          break;
        case OtpInfo.Type.FORGET_PASSWORD:
          body = this.genBodySendNotification(otpInfo.getOtpCode());
          break;
        default:
          return;
      }
      if(mailServer.equals(SMTP)) {
        sendEmailSmtp(mailHost, mailPort, mailSmtp, mailPass, mailSmtp, mailDomainName, otpInfo.getTo(), this.genSubjectSendNotification("[RESTAURANT]"), body);
      }
    }
    if (otpInfo.getMethod().equals(OtpInfo.Method.TELEGRAM)) {
      sendTelegram(telegramToken, telegramChatId, this.genSubjectSendNotification("[RESTAURANT]") + " Mã OTP xác nhận tài khoản của bạn" + otpInfo.getTo() + " là: " + otpInfo.getOtpCode(), body.replaceAll("<br>", "\n"));
    }
  }

  private String genBodySendNotification(String otpCode) {
    String body = "";
    body += "<b>Kính gửi Quý khách</b><br>";
    body += "Mã OTP của bạn là: <b>" + otpCode + "</b><br><br>";
    body += "Mã OTP này có hiệu lực trong [5 phút]. Vui lòng không chia sẻ mã OTP này với bất kỳ ai để đảm bảo tính bảo mật cho tài khoản của bạn.<br><br>";
    body += "Cảm ơn Quý khách đã sử dụng dịch vụ của toi.<br>";
    body += "Trân trọng<br>";
    return body;
  }

  private String genSubjectSendNotification(String area) {
    return area;
  }


  public void sendByMailgun(String apiKey, String domainName, String from, String fromName, String to, String subject, String body) {
    try {
      Configuration configuration = new Configuration()
          .domain(domainName).apiUrl("https://api.mailgun.net/v3")
          .apiKey(apiKey).from(fromName, from);
      Response response = Mail.using(configuration).to(to)
          .subject(subject).html(body)
          .build().send();
      log.debug("Response from server: {}", response.responseMessage());
    } catch (Exception e) {
      log.error("Error: {}", e.getMessage());
    }
  }

  public void sendTelegram(String botToken, String chatId, String subject, String message) {
    String urlString = "https://api.telegram.org/bot%s/sendMessage";
    try {
      urlString = String.format(urlString, botToken);
      Map<String, Object> data = new HashMap<>();
      data.put("chat_id", chatId);
      data.put("parse_mode", "HTML");
      data.put("text", subject + "\n" + message);
      // Set headers
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> entity = new HttpEntity<>(JsonParser.toJson(data), headers);
      restTemplate.postForEntity(urlString, entity, String.class);
    } catch (Exception e) {
      log.error("Error: {}", e.getMessage(), e);
    }
  }

  private void sendEmailSmtp(String smtpHost, String smtpPort, String emailSmtp, String password, String fromEmail, String fromName, String to, String subject, String body) {
    try{
      Properties properties = new Properties();
      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.starttls.enable", "true");
      properties.put("mail.smtp.starttls.required", "true");
      properties.put("mail.smtp.host", smtpHost);
      properties.put("mail.smtp.port", smtpPort);
      properties.put("mail.smtp.ssl.trust", smtpHost);

      Session session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(emailSmtp, password);
        }
      });

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(fromEmail, fromName, "UTF-8"));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
      message.setSubject(subject);
      message.setContent(body, "text/html; charset=utf-8");

      Transport.send(message);
      log.debug("===============Email sent successfully: {}", message);
    } catch (Exception e) {
      log.error("===============Error sending email: " + e.getMessage());
    }
  }
}
