package com.example.s3uploadtest.multipart.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MultipartService {

    private final S3Client s3Client;

    @Getter
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public Double uploadViaMultipart(MultipartFile file) throws IOException {
        double startTime = System.currentTimeMillis();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getOriginalFilename())
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        double endTime = System.currentTimeMillis();
        return (endTime - startTime) / 1000;
    }

    public List<String> uploadMultipleViaMultipart(MultipartFile[] files) throws IOException {
        return Arrays.stream(files)
                .map(file -> {
                    try {
                        double startTime = System.currentTimeMillis();

                        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(file.getOriginalFilename())
                                .contentType(file.getContentType())
                                .contentLength(file.getSize())
                                .build();

                        s3Client.putObject(putObjectRequest,
                                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

                        double endTime = System.currentTimeMillis();
                        double duration = (endTime - startTime) / 1000;

                        return file.getOriginalFilename() + " 업로드 완료 (" + duration + "초)";
                    } catch (IOException e) {
                        throw new UncheckedIOException("파일 업로드 실패: " + file.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());
    }
}

