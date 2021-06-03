package com.jedreck.cloud01sentinel.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(tags = "测试01")
@RestController
@RequestMapping("/t01")
public class T01Controller {

    public static final String HELLO = "/hello";
    public static final String PING = "/ping";
    public static final String SLOW = "/slow";

    @GetMapping(HELLO)
    @SentinelResource(value = HELLO)
    public String hello() {
        return "Hello";
    }

    @GetMapping(value = PING)
    @SentinelResource(value = PING, fallback = "slowFallback", blockHandler = "slowExceptionHandler")
    public String ping() {
        return "pong";
    }

    // Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
    public String pingFallback() {
        return "pingFallback";
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String pingExceptionHandler(BlockException ex) {
        // Do some log here.
        ex.printStackTrace();
        return "pingExceptionHandler";
    }

    @GetMapping(SLOW)
    @SentinelResource(value = SLOW, fallback = "slowFallback", blockHandler = "slowExceptionHandler")
    public String slow(@RequestParam(value = "i", required = false) Integer i) {
        int j = 1 / i;
        ThreadUtil.sleep(RandomUtil.randomInt(1, i), TimeUnit.SECONDS);
        return "slow";
    }

    // Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
    public String slowFallback(Integer s) {
        return String.format("helloFallback %d", s);
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String slowExceptionHandler(Integer s, BlockException ex) {
        // Do some log here.
        ex.printStackTrace();
        return "Oops, error occurred at " + s;
    }

    static {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource(PING);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 1.
        rule.setCount(1);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

}
