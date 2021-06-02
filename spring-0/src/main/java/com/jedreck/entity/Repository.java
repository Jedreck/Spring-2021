package com.jedreck.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Data
@Component(value = "myRepo")
public class Repository {
    @Autowired
    @Qualifier("ds")
    private DataSource dataSource;
}
