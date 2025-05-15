package com.example.s3uploadtest.presignedurl.controller;

import com.example.s3uploadtest.presignedurl.service.PresignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PresignedUrlController {

    private final PresignedUrlService presignedUrlService;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @GetMapping("/s3/presigned-url")
    public String getPresignedUrl(@RequestParam String filename) {
        String key = "uploads/" + filename;
        return presignedUrlService.generatePresignedUrl(bucketName, key);
    }


}
