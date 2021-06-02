package com.jedreck.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component(value = "ds")
public class DataSource {
    @Value("root")
    private String user;
    @Value("root123")
    private String password;
    @Value("url")
    private String url;
    @Value("driverName")
    private String driverName;
}
