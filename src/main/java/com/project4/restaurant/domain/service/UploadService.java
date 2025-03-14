//package com.wiinvent.vnta.domain.service;
//
//import com.wiinvent.vnta.app.agency.response.UploadResponse;
//import com.wiinvent.vnta.domain.core.exception.BadRequestException;
//import com.wiinvent.vnta.domain.util.GeneratorUtil;
//import com.wiinvent.vnta.domain.util.Helper;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.URISyntaxException;
//import java.util.List;
//import java.util.Objects;
//
//@Service
//@Log4j2
//public class UploadService {
//  private final List<String> contentTypeAllowList = List.of("image/jpeg", "image/png", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/msword", "application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/csv", "application/vnd.ms-excel");
//
//  private final List<String> CONTENT_TYPE_ALLOW_ATTACH_FILE = List.of("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/msword", "application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/csv", "application/vnd.ms-excel");
//
//  private final String S3 = "s3";
//  private final String LOCAL = "local";
//
//  @Value("${file.upload-type}")
//  private String fileUploadType;
//
//  @Value("${file.dir}")
//  private String fileDir;
//
//  @Value("${file.url}")
//  private String fileUrl;
//
//  private final S3Service s3Service;
//
//  public UploadService(S3Service s3Service) {
//    this.s3Service = s3Service;
//  }
//
//  public UploadResponse uploadStatic(MultipartFile file) throws IOException, URISyntaxException {
//    if (file == null) {
////      throw new BadRequestException("Missing file input");
//      throw new BadRequestException("Thiếu tập tin đầu vào");
//    }
////    if (file.getSize() > 1000000) {
////      throw new BadRequestException("File size not valid");
////    }
//    if (!contentTypeAllowList.contains(file.getContentType())) {
////      throw new BadRequestException("File not match input");
//      throw new BadRequestException("Định dạng file không được hỗ trợ");
//    }
//
//    return this.uploadFileByType(file);
//  }
//
//  public UploadResponse uploadStaticAttachFile(MultipartFile file) throws IOException, URISyntaxException {
//    if (file == null) {
//      throw new BadRequestException("Thiếu file đầu vào");
//    }
//    if (!CONTENT_TYPE_ALLOW_ATTACH_FILE.contains(file.getContentType())) {
//      throw new BadRequestException("File sai định dạng (chỉ hỗ trợ .doc, .pdf, .xls, .xlsx, .csv)");
//    }
//
//    return this.uploadFileByType(file);
//  }
//
//  public UploadResponse uploadFileByType(MultipartFile file) throws IOException, URISyntaxException {
//    if (fileUploadType == null) {
//      return this.uploadLocal(file);
//    }
//    if (fileUploadType.equals(LOCAL)) {
//      return this.uploadLocal(file);
//    }
//    if (fileUploadType.equals(S3)) {
//      String fileName = getFileName(file);
//      fileName = Helper.boDauTiengViet(fileName);
//      File fileTemp = this.convertMultiPartToFile(file, fileName);
//      UploadResponse response = s3Service.putFile(fileTemp, GeneratorUtil.genSubPathFile() + File.separator + fileName);
//      fileTemp.delete();
//      return response;
//    }
//    return this.uploadLocal(file);
//  }
//
//
//  private UploadResponse uploadLocal(MultipartFile file) {
//    try {
//      String fileName = getFileName(file);
//      fileName = Helper.boDauTiengViet(fileName);
//      String subPath = File.separator + GeneratorUtil.genSubPathFile();
//
//      File rootFolder = new File(fileDir + subPath);
//      if (!rootFolder.exists()) {
//        rootFolder.mkdirs();
//      }
//
//      File newFile = new File(fileDir + subPath + File.separator + fileName);
//      if (!newFile.exists()) {
//        rootFolder.createNewFile();
//      }
//
//      FileCopyUtils.copy(file.getBytes(), newFile);
//
//      return UploadResponse.builder()
//          .fileUrl(fileUrl + subPath + File.separator + fileName)
//          .fileName(newFile.getName())
//          .fileSize(file.getSize())
//          .build();
//
//    } catch (Exception e) {
//      log.error(e.getMessage());
//      throw new BadRequestException("Co loi xay ra");
//    }
//  }
//
//  public String getFileName(MultipartFile file) {
//    String fileName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0].trim();
//    fileName = Helper.boDauTiengViet(fileName).replaceAll(" +", "_").toLowerCase() + "_" + System.currentTimeMillis();
//
//    if (Objects.equals(file.getContentType(), "image/png")) {
//      fileName = fileName + ".png";
//    } else if (Objects.equals(file.getContentType(), "image/jpg")) {
//      fileName = fileName + ".jpg";
//    } else if (Objects.equals(file.getContentType(), "image/jpeg")) {
//      fileName = fileName + ".jpeg";
//    } else if (Objects.equals(file.getContentType(), "application/vnd.android.package-archive")) {
//      fileName = fileName + ".apk";
//    } else if (Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//      fileName = fileName + ".xlsx";
//    } else if (Objects.equals(file.getContentType(), "application/msword")) {
//      fileName = fileName + ".docx";
//    } else if (Objects.equals(file.getContentType(), "application/pdf")) {
//      fileName = fileName + ".pdf";
//    } else if (Objects.equals(file.getContentType(), "text/csv")) {
//      fileName = fileName + ".csv";
//    } else if (Objects.equals(file.getContentType(), "application/vnd.ms-excel")) {
//      fileName = fileName + ".xls";
//    }
//    return fileName;
//  }
//
//  private File convertMultiPartToFile(MultipartFile file, String fileName) throws IOException {
//    File convFile = new File(Objects.requireNonNull(Helper.boDauTiengViet(fileName)));
//    try (FileOutputStream fos = new FileOutputStream(convFile)) {
//      fos.write(file.getBytes());
//    }
//    return convFile;
//  }
//}
