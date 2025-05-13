package com.example.s3uploadtest.stream.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StreamService {

    private final S3Client s3Client;

    @Getter
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public Double uploadViaStream(HttpServletRequest request, String fileName) throws IOException {
        double startTime = System.currentTimeMillis();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(request.getContentType())
                .contentLength(request.getContentLengthLong())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(request.getInputStream(), request.getContentLengthLong()));
        double endTime = System.currentTimeMillis();
        return (endTime - startTime) / 1000;
    }

}
