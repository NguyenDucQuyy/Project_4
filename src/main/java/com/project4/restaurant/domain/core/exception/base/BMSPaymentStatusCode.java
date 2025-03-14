//package com.wiinvent.vnta.domain.core.exception.base;
//
//import com.wiinvent.vnta.domain.pojo.LanguageModel;
//import lombok.Getter;
//
//public enum BMSPaymentStatusCode {
//  SUCCESS(1, LanguageModel.builder()
//      .default_lang("Thành công")
//      .vi_VN("Thành công")
//      .en_US("Thành công")
//      .build()),
//  NOT_FOUND(-10, LanguageModel.builder()
//      .default_lang("Mã giao dịch chưa tồn tại trên hệ thống")
//      .vi_VN("Mã giao dịch chưa tồn tại trên hệ thống")
//      .en_US("Mã giao dịch chưa tồn tại trên hệ thống")
//      .build()),
//  NONE(-1, LanguageModel.builder()
//      .default_lang("Giao dịch chưa xác định")
//      .vi_VN("Giao dịch chưa xác định")
//      .en_US("Giao dịch chưa xác định")
//      .build());
//
//  @Getter
//  private final int code;
//  private final LanguageModel languageModel;
//
//  BMSPaymentStatusCode(int code, LanguageModel languageModel) {
//    this.code = code;
//    this.languageModel = languageModel;
//  }
//
//  public String getLabel() {
//    return this.languageModel.getDefault_lang();
//  }
//}
