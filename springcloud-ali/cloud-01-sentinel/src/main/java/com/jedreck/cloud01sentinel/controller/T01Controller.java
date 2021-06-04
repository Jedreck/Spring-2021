package com.jedreck.cloud01sentinel.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
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
@Api(tags = "流量控制")
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
    @SentinelResource(value = PING, fallback = "pingFallback", blockHandler = "pingExceptionHandler")
    public String ping() {
        return "pong";
    }

    // Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
    // 不能是private
    public String pingFallback() {
        log.error("pingFallback");
        return "pingFallback";
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String pingExceptionHandler(BlockException ex) {
        log.error("pingExceptionHandler");
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

    /**
     * 流量控制
     * https://sentinelguard.io/zh-cn/docs/flow-control.html
     */
    public static List<FlowRule> t01Rules() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();

        // 资源名称，同一个资源可以同时有多个限流规则
        rule.setResource(PING);
        // 限流阈值类型
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 限流阈值
        rule.setCount(10);
        // 流控针对的调用来源，default，代表不区分调用来源
        rule.setLimitApp(RuleConstant.LIMIT_APP_DEFAULT);
        // 调用关系限流策略：直接(默认)、链路、关联
        rule.setStrategy(RuleConstant.STRATEGY_DIRECT);

        // 流控效果（直接拒绝 / 排队等待 / 慢启动模式），不支持按调用关系限流
        // 该方式主要用于系统长期处于低水位的情况下，当流量突然增加时，直接把系统拉升到高水位可能瞬间把系统压垮。通过"冷启动"，让通过的流量缓慢增加，在一定时间内逐渐增加到阈值上限，给冷系统一个预热的时间，避免冷系统被压垮的情况。
//        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
//        // 时长
//        rule.setWarmUpPeriodSec(5);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
        rule.setMaxQueueingTimeMs(2 * 1000);
        rules.add(rule);

        return rules;
    }

}
