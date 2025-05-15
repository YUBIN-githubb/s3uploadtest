package com.example.s3uploadtest.aws_multipart.dto;

import com.example.s3uploadtest.aws_multipart.controller.AwsMultipartController;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompleteRequest {
    private String uploadId;
    private String filename;
    private List<PartETag> parts;
}
