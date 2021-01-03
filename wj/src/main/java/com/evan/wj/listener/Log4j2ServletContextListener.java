package com.evan.wj.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * log4j2的监听类
 */
@Component
public class Log4j2ServletContextListener implements ServletContextListener{
    @Value("${log4j2.appender.path}")
    private String log4j2Path;

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.setProperty("LogHomeRoot", log4j2Path);
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.getProperties().remove("LogHomeRoot");
    }
}