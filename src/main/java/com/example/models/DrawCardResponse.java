package com.example.models;

import lombok.Data;

import java.util.List;

@Data
public class DrawCardResponse extends BaseResponse {

    private List<Card> cards;
}
