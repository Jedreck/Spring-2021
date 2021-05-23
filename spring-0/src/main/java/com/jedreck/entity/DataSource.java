package com.jedreck.entity;

import lombok.Data;

@Data
public class DataSource {
    private String user;
    private String password;
    private String url;
    private String driverName;
}
