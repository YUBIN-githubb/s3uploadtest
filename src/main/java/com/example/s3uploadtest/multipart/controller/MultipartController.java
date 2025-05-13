package com.example.s3uploadtest.multipart.controller;

import com.example.s3uploadtest.multipart.service.MultipartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MultipartController {

    private final MultipartService multipartService;

    @PostMapping("/multipart")
    public ResponseEntity<String> uploadMultipart(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(multipartService.uploadViaMultipart(file) + "초");
    }

    @PostMapping("/multipart/multiple")
    public ResponseEntity<List<String>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        List<String> result = multipartService.uploadMultipleViaMultipart(files);
        return ResponseEntity.ok(result);
    }
}
