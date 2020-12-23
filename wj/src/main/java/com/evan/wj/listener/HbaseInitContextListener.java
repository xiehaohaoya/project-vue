package com.evan.wj.listener;

import com.evan.wj.utils.HbaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class HbaseInitContextListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    HbaseUtils hbaseUtils;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        hbaseUtils.initRepository();
        for (int i = 0; i < 10; i++) {
            System.out.println("love a==================");
        }

    }
}
