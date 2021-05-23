package com.jedreck.entity;

import lombok.Data;

@Data
public class Account {
    private Integer id;
    private String name;

    public Account() {
        System.out.println("无参构造:Account");
    }
}
