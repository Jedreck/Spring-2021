package com.jedreck.entity;

import lombok.Data;

@Data
public class Student {
    private Integer id;
    private String name;
    private Integer age;
    private Classes classes;

    public Student(){
        System.out.println("无参构造");
    }

    public Student(Integer id,Integer age){
        this.id = id;
        this.age = age;
        System.out.println("使用有参");
    }
}
