package com.jedreck.entity;

import lombok.Data;

@Data
public class Person {
    private Integer Id;
    private String name;
    private Car car;
}
