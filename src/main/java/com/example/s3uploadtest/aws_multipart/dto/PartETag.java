package com.example.s3uploadtest.aws_multipart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartETag {
    private int partNumber;
    @JsonProperty("eTag")
    private String eTag;
}
