package com.example.models;

import lombok.Data;

@Data
public class Card {

    private String code;
    private String image;
    private Images images;
    private String value;
    private String suit;

    @Data
    class Images {
        private String svg;
        private String png;
    }
}
