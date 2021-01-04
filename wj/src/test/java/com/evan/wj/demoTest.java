package com.evan.wj;

import com.evan.wj.pojo.udp.ResendKeyPojo;
import com.evan.wj.utils.udp.ParseUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class demoTest {

    /**
     * 查看空闲内存
     */
    @Test
    public void getFreeMemory() {
        System.out.println(Runtime.getRuntime().freeMemory());
    }

    /**
     * 高并发线程安全集合
     */
    @Test
    public void examAreaConcurrentHashMap() {
        Map<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Object> concurrentHashMap1 = new ConcurrentHashMap<>();
        concurrentHashMap.put("key1",1);
        concurrentHashMap.remove("key1");

        ((ConcurrentHashMap)concurrentHashMap).elements();

        if (concurrentHashMap instanceof ConcurrentHashMap) {
            ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) concurrentHashMap;
        }
    }

    /**
     * 线程池测试
     */
    @Test
    public void aeroThreadPool() {
        for (int num = 0; num < 6; num++) {
            log.info("loading......");
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        AeroThread aeroThread1 = new AeroThread();
        AeroThread aeroThread2 = new AeroThread();
        AeroThread aeroThread3 = new AeroThread();

        executorService.execute(aeroThread1);
        executorService.execute(aeroThread2);
        executorService.execute(aeroThread3);
    }

    @Test
    public void function1() {

    }

    @Test
    public void function2() {

    }

    @Test
    public void function3() {

    }

    @Test
    public void function4() {

    }
}

@Slf4j
class AeroThread extends Thread {
    @Override
    public void run() {
        log.info("正在执行:{}",Thread.currentThread().getName());
        System.out.println("正在执行:{}"+Thread.currentThread().getName());
    }

    public void function() {
        System.out.println("function方法");
    }
}
