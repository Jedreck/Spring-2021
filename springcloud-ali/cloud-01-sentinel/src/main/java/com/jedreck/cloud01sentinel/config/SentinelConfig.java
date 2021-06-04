package com.jedreck.cloud01sentinel.config;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.jedreck.cloud01sentinel.controller.T01Controller;
import com.jedreck.cloud01sentinel.controller.T02Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class SentinelConfig implements CommandLineRunner {

    @Override
    public void run(String... args) {
        // 限流
        List<FlowRule> flowRules = new ArrayList<>();
        flowRules.addAll(T01Controller.t01Rules());
        FlowRuleManager.loadRules(flowRules);

        // 熔断
        List<DegradeRule> degradeRules = new ArrayList<>();
        degradeRules.addAll(T02Controller.getDegradeRule());
        DegradeRuleManager.loadRules(degradeRules);

        // 系统守护
        List<SystemRule> systemRules = new ArrayList<>();
        systemRules.addAll(getSystemRule());
        SystemRuleManager.loadRules(systemRules);

        log.info("规则加载结束");
    }

    private static List<SystemRule> getSystemRule() {
        List<SystemRule> rules = new ArrayList<>();
        SystemRule rule = new SystemRule();
        // max load is 3
        rule.setHighestSystemLoad(3.0);
        // max cpu usage is 60%
        rule.setHighestCpuUsage(0.6);
        // max avg rt of all request is 10 ms
        rule.setAvgRt(10);
        // max total qps is 20
        rule.setQps(20);
        // max parallel working thread is 10
        rule.setMaxThread(10);

        rules.add(rule);

        return rules;
    }
}
