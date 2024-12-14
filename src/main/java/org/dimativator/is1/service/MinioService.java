package org.dimativator.is1.service;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {
    
    @Value("${minio.bucket.name}")
    private String bucketName;
    
    private final MinioClient minioClient;

    public String prepareUpload(MultipartFile file) throws Exception {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String tempBucketName = bucketName + "-temp";
        
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(tempBucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(tempBucketName).build());
        }
        
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(tempBucketName)
                .object(filename)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build()
        );
        
        return filename;
    }

    public void commitUpload(String filename) throws Exception {
        String tempBucketName = bucketName + "-temp";
        
        minioClient.copyObject(
            CopyObjectArgs.builder()
                .source(CopySource.builder().bucket(tempBucketName).object(filename).build())
                .bucket(bucketName)
                .object(filename)
                .build()
        );
        
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(tempBucketName)
                .object(filename)
                .build()
        );
    }

    public void rollbackUpload(String filename) throws Exception {
        String tempBucketName = bucketName + "-temp";
        
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(tempBucketName)
                .object(filename)
                .build()
        );
    }

    public InputStream downloadFile(String filename) throws Exception {
        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .build()
        );
    }
}