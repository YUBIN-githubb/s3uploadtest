package com.example.s3uploadtest.aws_multipart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.UploadPartPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AwsMultipartService {

    private final S3Client s3Client;

    // 1. 멀티파트 업로드 시작
    public String initiateMultipartUpload(String bucket, String key) {
        CreateMultipartUploadRequest request = CreateMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        return s3Client.createMultipartUpload(request).uploadId();
    }

    // 2. 각 파트별 presigned URL 생성
    public URL generatePresignedUrlForPart(String bucket, String key, String uploadId, int partNumber) {
        S3Presigner presigner = S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(s3Client.serviceClientConfiguration().credentialsProvider())
                .build();

        UploadPartRequest partRequest = UploadPartRequest.builder()
                .bucket(bucket)
                .key(key)
                .uploadId(uploadId)
                .partNumber(partNumber)
                .build();

        UploadPartPresignRequest presignRequest = UploadPartPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .uploadPartRequest(partRequest)
                .build();

        URL url = presigner.presignUploadPart(presignRequest).url();
        presigner.close();
        return url;
    }

    // 3. 완료 요청 (ETag 목록 필요)
    public void completeMultipartUpload(String bucket, String key, String uploadId, List<CompletedPart> parts) {
        CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
                .parts(parts)
                .build();

        CompleteMultipartUploadRequest completeRequest = CompleteMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(key)
                .uploadId(uploadId)
                .multipartUpload(completedMultipartUpload)
                .build();

        s3Client.completeMultipartUpload(completeRequest);
    }
}
