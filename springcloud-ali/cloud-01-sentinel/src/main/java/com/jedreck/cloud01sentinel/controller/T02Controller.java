package com.jedreck.cloud01sentinel.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Api(tags = "熔断降级")
@RestController
@RequestMapping("/t02")
public class T02Controller {
    public static final String HELLO = "/hello02";

    @GetMapping(HELLO)
    @SentinelResource(value = HELLO, fallback = "fallback", blockHandler = "block")
    public String hello() {
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        if (RandomUtil.randomBoolean()) {
            throw new RuntimeException("异常");
        }
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        return "Hello";
    }

    public String fallback() {
        log.error("fallback");
        return "fallback";
    }

    public String block(BlockException ex) {
        log.error("fallback");
        return "fallback";
    }

    /**
     * 慢调用比例 (SLOW_REQUEST_RATIO)：选择以慢调用比例作为阈值，需要设置允许的慢调用 RT（即最大的响应时间），请求的响应时间大于该值则统计为慢调用。当单位统计时长（statIntervalMs）内请求数目大于设置的最小请求数目，并且慢调用的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求响应时间小于设置的慢调用 RT 则结束熔断，若大于设置的慢调用 RT 则会再次被熔断。
     * 异常比例 (ERROR_RATIO)：当单位统计时长（statIntervalMs）内请求数目大于设置的最小请求数目，并且异常的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。异常比率的阈值范围是 [0.0, 1.0]，代表 0% - 100%。
     * 异常数 (ERROR_COUNT)：当单位统计时长内的异常数目超过阈值之后会自动进行熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。
     */
    public static List<DegradeRule> getDegradeRule() {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule(HELLO)
                // 熔断策略，支持慢调用比例/异常比例/异常数策略
//                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
//                // 慢调用比例阈值，仅慢调用比例模式有效（1.8.0 引入）
//                .setSlowRatioThreshold(0.6)
                .setGrade(CircuitBreakerStrategy.ERROR_COUNT.getType())

                // 慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值
                .setCount(5)
                // 熔断时长，单位为 s
                .setTimeWindow(5)
                // 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入）
                .setMinRequestAmount(5)
                // 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）
                .setStatIntervalMs(5*1000);
        rules.add(rule);

        return rules;
    }

}
