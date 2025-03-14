package com.project4.restaurant.domain.service.notification;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Log4j2
public class FCMInitializer {
  @Value("${firebase.base-64-config}")
  private String firebaseConfigBase64;

  @PostConstruct
  public void initialize() {
    try {
      byte[] decodedBytes = Base64.decodeBase64(firebaseConfigBase64);
      InputStream serviceAccount = new ByteArrayInputStream(decodedBytes);

      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build();
      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
        log.info("Firebase application has been initialized");
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}