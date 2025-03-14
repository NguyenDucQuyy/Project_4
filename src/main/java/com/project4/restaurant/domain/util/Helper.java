package com.project4.restaurant.domain.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class Helper {
  public static final String ZONE_UTC = "UTC";
  public static final String ZONE_VN = "Asia/Ho_Chi_Minh";

  public static String getCurrentTimeString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    ZoneId zoneId = ZoneId.of(ZONE_VN);
    return LocalDateTime.now(zoneId).format(formatter);
  }

  public static long getNowMillisAtUtc() {
    return LocalDateTime.now(ZoneId.of(ZONE_UTC)).toInstant(ZoneOffset.UTC).toEpochMilli();
  }

  public static long getNowMillisAtVn() {
    return ZonedDateTime.now(ZoneId.of(ZONE_VN)).toInstant().toEpochMilli();
  }


  public static long timeToLongAtUtc(LocalDateTime dateTimeAtUtc) {
    return ZonedDateTime.of(dateTimeAtUtc, ZoneId.of(ZONE_UTC)).toInstant().toEpochMilli();
  }

  public static LocalDateTime getNowDateTimeAtUtc() {
    return LocalDateTime.now(ZoneId.of(ZONE_UTC));
  }

  public static LocalDate getNowDateAtUtc() {
    return getNowDateTimeAtUtc().toLocalDate();
  }

  public static LocalDateTime longToLocalDateTime(Long millisAtUtc) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(millisAtUtc), ZoneId.of(ZONE_UTC));
  }

  public static LocalDate longToLocalDate(Long millisAtUtc) {
    return LocalDate.ofInstant(Instant.ofEpochMilli(millisAtUtc), ZoneId.of(ZONE_UTC));
  }

  public static Long localdateToLong(LocalDate localDate) {
    return localDate.toEpochDay();
  }
  // Hàm tính số ngày giữa ngày giao nhiệm vụ và ngày hạn xử lý
  public static LocalDate calculateDeadLineDay(LocalDate assignedDate, LocalDate dueDate) {

    // Tính số ngày giữa 2 ngày
    long daysBetween = ChronoUnit.DAYS.between(assignedDate, dueDate);

    long daysToDeadLine = Math.round(daysBetween * 0.80);

    //LocalDate currentDate = getNowDateAtVn();

    return assignedDate.plusDays(daysToDeadLine);
  }

  public static LocalDateTime getNowDateTimeAtVn() {
    return LocalDateTime.now(ZoneId.of(ZONE_VN));
  }

  public static LocalDate getNowDateAtVn() {
    return getNowDateTimeAtVn().toLocalDate();
  }

  public static LocalDateTime getTodayDateTimeHCMZone(){
    return LocalDateTime.now(ZoneId.of(ZONE_VN));
  }

  public static long timeToLongAtVn(LocalDateTime dateTimeAtVn) {
    return ZonedDateTime.of(dateTimeAtVn, ZoneId.of(ZONE_VN)).toInstant().toEpochMilli();
  }

  public static long getStartOfDay(LocalDate date) {
    LocalDateTime localDateTime = date.atStartOfDay();
    return localDateTime
        .atZone(ZoneId.of(ZONE_VN))
        .withZoneSameInstant(ZoneId.of(ZONE_UTC))
        .toInstant()
        .toEpochMilli();
  }

  public static Long getEndOfDay(LocalDate date) {
    LocalDateTime localDateTime = date.atStartOfDay().plusDays(1).minusSeconds(1);
    return localDateTime
        .atZone(ZoneId.of(ZONE_VN))
        .withZoneSameInstant(ZoneId.of(ZONE_UTC))
        .toInstant()
        .toEpochMilli();
  }
  public static String localDateToString(LocalDate date, String pattern) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    return date.format(formatter);
  }
  public static String md5Token(String input) {
    return DigestUtils.sha256Hex(input);
  }

  public static OffsetDateTime convertLongToOffsetDateTimeUTC(long time) {
    Instant instant = Instant.ofEpochMilli(time);
    OffsetDateTime offsetDateTimeUTC = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    return offsetDateTimeUTC;
  }

  public static LocalDate convertStringToLocalDate(String dateStr, String pattern) {
    if (dateStr == null || dateStr.isEmpty()) {
      return null; // Trả về null nếu chuỗi ngày trống
    }
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern); // Định nghĩa định dạng
      return LocalDate.parse(dateStr, formatter); // Chuyển đổi sang LocalDate
    } catch (DateTimeParseException e) {
      throw new RuntimeException("Sai định dạng ngày: " + dateStr + ". Định dạng đúng: " + pattern, e);
    }
  }

  public static Long convertLocalDateTimeToLong(LocalDateTime localDateTime) {
    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(ZONE_VN));
    return zonedDateTime.toInstant().toEpochMilli();
  }

  public static String getPath(HttpServletRequest request) {
    StringBuilder requestURL = new StringBuilder(request.getRequestURI());
    String queryString = request.getQueryString();
    return queryString == null ? requestURL.toString() : requestURL.append('?').append(queryString).toString();
  }

  public static String stringFormatDay(int value) {
    if (value < 10) return "0" + value;
    return value + "";
  }

  public static boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  public static boolean isNumeric(String str) {
    if (str == null || str.trim().isEmpty()) {
      return false;
    }

    // Loại bỏ khoảng trắng đầu cuối
    str = str.trim();

    // Kiểm tra nếu chuỗi chỉ chứa dấu trừ hoặc dấu chấm
    if (str.equals("-") || str.equals(".")) {
      return false;
    }

    boolean hasDecimal = false;

    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);

      if (c == '-' && i == 0) {
        continue;
      }

      if (c == '.') {
        if (hasDecimal || i == 0 || i == str.length() - 1) {
          return false;
        }
        hasDecimal = true;
        continue;
      }

      // Kiểm tra ký tự phải là số
      if (!Character.isDigit(c)) {
        return false;
      }
    }
    return true;
  }
}
