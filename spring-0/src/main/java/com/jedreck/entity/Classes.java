package com.jedreck.entity;

import lombok.Data;

import java.util.List;

@Data
public class Classes {
    private Integer id;
    private String name;
    private List<Integer> list;
}
