package com.evan.wj.listener;

import com.evan.wj.utils.HbaseUtils;
import com.evan.wj.utils.SingletonThreadPool;
import com.evan.wj.utils.udp.ReSendThread;
import com.evan.wj.utils.udp.UdpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * 在程序启动时就要跑起来的程序在这里调用
 */
@Component
public class InitBootContextListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    HbaseUtils hbaseUtils;
    @Autowired
    UdpServer udpServer;
    @Autowired
    ReSendThread reSendThread;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //初始化hbase，使用时解开项目的注解
//        hbaseUtils.initRepository();

        //初始化udpServer的接收器
        udpServer.udpReceive();

        ExecutorService executor = SingletonThreadPool.getExecutor();
        executor.execute(reSendThread);
    }
}