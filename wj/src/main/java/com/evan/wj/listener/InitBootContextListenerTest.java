package com.evan.wj.listener;

import com.evan.wj.utils.SingletonThreadPool;
import com.evan.wj.utils.udp.ReSendThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * 在程序启动时就要跑起来的程序在这里调用
 */
@Component
public class InitBootContextListenerTest implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    ReSendThread reSendThread;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        // 用来测试udp的三次重传机制
        ExecutorService executor = SingletonThreadPool.getExecutor();
        executor.execute(reSendThread);
    }
}