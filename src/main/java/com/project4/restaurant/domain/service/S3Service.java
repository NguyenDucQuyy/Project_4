//package com.wiinvent.vnta.domain.service;
//
//import com.wiinvent.vnta.app.agency.response.UploadResponse;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3AsyncClient;
//import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
//import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
//import software.amazon.awssdk.transfer.s3.S3TransferManager;
//import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
//import software.amazon.awssdk.transfer.s3.model.FileUpload;
//import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
//import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;
//
//import java.io.File;
//import java.net.URI;
//import java.net.URISyntaxException;
//
//@Service
//@Log4j2
//public class S3Service {
//
//  @Value("${s3.access-key}")
//  private String accessKey;
//
//  @Value("${s3.secret-key}")
//  private String secretKey;
//
//  @Value("${s3.end-point}")
//  private String endPoint;
//
//  @Value("${s3.bucket-name}")
//  private String bucketName;
//
//  public UploadResponse putFile(File file, String key) throws URISyntaxException {
//    log.debug("putFile Uploading a new object to S3 from a file {}", file.getName());
//
//    long contentLength = file.length();
//    log.debug("putFile File size : {} => KB: {} => MB: {}", contentLength, contentLength / 1024, contentLength / 1024 / 1024);
//    try {
//      StaticCredentialsProvider staticCredentialsProvider =
//          StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
//      S3AsyncClientBuilder builder = S3AsyncClient.builder();
//      builder.credentialsProvider(staticCredentialsProvider);
//      // TODO: check the region
//      builder.region(Region.US_EAST_2);
//      builder.endpointOverride(new URI(endPoint));
//      builder.forcePathStyle(true);
//      S3AsyncClient s3AsyncClient = builder.build();
//
//      S3TransferManager transferManager =
//          S3TransferManager.builder().s3Client(s3AsyncClient).build();
//
//      UploadFileRequest uploadFileRequest =
//          UploadFileRequest.builder()
//              .putObjectRequest(
//                  req -> req.bucket(bucketName).key(key).acl(ObjectCannedACL.PUBLIC_READ))
//              .addTransferListener(LoggingTransferListener.create())
//              .source(file)
//              .build();
//      FileUpload upload = transferManager.uploadFile(uploadFileRequest);
//      CompletedFileUpload uploadResult = upload.completionFuture().join();
//      log.debug("putFile eTag: {}", uploadResult.response().eTag());
//      log.debug("putFile Push file Successful : {} to S3 => KB: {} => MB: {}", key, contentLength / 1024, contentLength / 1024 / 1024);
//      transferManager.close();
////      return UploadMediaResponse.builder().url(genMediaFileUrl(key)).urlFolder(key).build();
//      return UploadResponse.builder()
//          .fileName(file.getName())
//          .fileUrl(endPoint + "/" + bucketName + "/" + key)
//          .fileSize(contentLength)
//          .build();
//    } catch (Exception e) {
//      log.error("putFile Push file Failed : {} to S3 => KB: {} => MB: {} ==> error message: {}", key, contentLength / 1024, contentLength / 1024 / 1024, e.getMessage());
//      throw e;
//    }
//  }
//
//}
