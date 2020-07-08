package com.example.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpResponse {

    private int status;
    private String content;
}
