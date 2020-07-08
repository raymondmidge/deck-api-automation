package com.example.models;

import lombok.Data;

@Data
public abstract class BaseResponse {

    private Boolean success;
    private String deck_id;
    private Integer remaining;
    private String error;
}
