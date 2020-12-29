package com.evan.wj.listener;

import com.evan.wj.utils.HbaseUtils;
import com.evan.wj.utils.udp.UdpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class InitBootContextListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    HbaseUtils hbaseUtils;
    @Autowired
    UdpServer udpServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //初始化hbase
//        hbaseUtils.initRepository();

        //初始化udpServer的接收器
        udpServer.udpReceive();
    }
}