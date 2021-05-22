package com.BOEC.service.util;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Date;
import java.util.List;

@Service
public class MinioAdapter {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucket.name}")
    String defaultBucketName;

    @Value("${minio.default.folder}")
    String defaultBaseFolder;

    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    public void uploadFile(MultipartFile file) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(defaultBucketName).object(generateFileName(file)).stream(
                            file.getInputStream(), -1, 10485760).contentType("image/jpg").contentType("image/png")
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public String getFile(String key) {
//        try (InputStream finput = minioClient.getObject(
//                GetObjectArgs.builder()
//                        .bucket(defaultBucketName)
//                        .object(key)
//                        .build())) {
//            byte[] imageBytes = new byte[10485760];
//            finput.read(imageBytes, 0, imageBytes.length);
//            finput.close();
//            return Base64.encodeBase64String(imageBytes);
//        }
        try {
           return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(defaultBucketName)
                    .object(key)
                    .expiry(24 * 60 * 60)
                    .build());
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostConstruct
    public void init() {
    }
}