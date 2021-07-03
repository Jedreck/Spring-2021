package com.jedreck.elk_test_01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/t01")
public class Controller {
    @GetMapping("/1")
    public String t1() {
        log.debug("debug-01");
        log.info("info-01");
        log.warn("warn-01");
        log.error("error-01");
        return "done";
    }
}
