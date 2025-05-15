package com.example.s3uploadtest.aws_multipart.controller;

import com.example.s3uploadtest.aws_multipart.dto.CompleteRequest;
import com.example.s3uploadtest.aws_multipart.service.AwsMultipartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.model.CompletedPart;

import java.net.URL;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3/multipart")
public class AwsMultipartController {

    private final AwsMultipartService awsMultipartService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @GetMapping("/initiate")
    public String initiate(@RequestParam String filename) {
        return awsMultipartService.initiateMultipartUpload(bucketName, "uploads/" + filename);
    }

    @GetMapping("/presigned-url")
    public String getPartUploadUrl(@RequestParam String filename,
                                   @RequestParam String uploadId,
                                   @RequestParam int partNumber) {
        URL url = awsMultipartService.generatePresignedUrlForPart(bucketName, "uploads/" + filename, uploadId, partNumber);
        return url.toString();
    }

    @PostMapping("/complete")
    public String completeUpload(@RequestBody CompleteRequest request) {
        List<CompletedPart> parts = request.getParts().stream()
                .map(p -> software.amazon.awssdk.services.s3.model.CompletedPart.builder()
                        .partNumber(p.getPartNumber())
                        .eTag(p.getETag().replaceAll("^\"|\"$", ""))
                        .build())
                .toList();

        awsMultipartService.completeMultipartUpload(bucketName, "uploads/" + request.getFilename(), request.getUploadId(), parts);
        return "Upload Complete";
    }
}
