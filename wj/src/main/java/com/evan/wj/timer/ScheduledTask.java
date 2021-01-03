package com.evan.wj.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时器，在使用时解开注解
 */
@Slf4j
@EnableScheduling
@Component
public class ScheduledTask {
//    @Scheduled(cron="0 0/1 * * * ?")
//    public void testOne() {
//        log.info("每分钟执行一次");
//    }
//
//    @Scheduled(fixedRate=30000)
//    public void testTwo() {
//        log.info("每30秒执行一次");
//    }

}