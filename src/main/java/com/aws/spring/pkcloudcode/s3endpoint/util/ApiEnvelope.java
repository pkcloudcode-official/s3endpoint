package com.aws.spring.pkcloudcode.s3endpoint.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiEnvelope<T> {
    private String status;
    private String message;
    private T data;
    private Instant timestamp;
    private String path;
    private String traceId;
}
