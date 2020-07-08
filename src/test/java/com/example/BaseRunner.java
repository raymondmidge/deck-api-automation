package com.example;

import com.example.utils.HttpUtil;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class BaseRunner {

    protected static HttpUtil httpUtil;
    protected static Gson gson = new Gson();

    @BeforeAll
    static void setup(){
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            httpUtil = new HttpUtil(prop.getProperty("deck.api.url"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
