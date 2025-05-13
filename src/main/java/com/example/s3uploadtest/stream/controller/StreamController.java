package com.example.s3uploadtest.stream.controller;

import com.example.s3uploadtest.stream.service.StreamService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class StreamController {

    private final StreamService streamService;

    @PostMapping("/stream")
    public ResponseEntity<String> uploadStream(HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(streamService.uploadViaStream(request, request.getHeader("File-Name")) + "초");
    }
}
