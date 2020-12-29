package com.evan.wj;

import com.evan.wj.utils.udp.ParseUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
        AeroThread aeroThread4 = new AeroThread();
        AeroThread aeroThread5 = new AeroThread();
        AeroThread aeroThread6 = new AeroThread();

        executorService.execute(aeroThread1);
        executorService.execute(aeroThread2);
        executorService.execute(aeroThread3);
        executorService.execute(aeroThread4);
        executorService.execute(aeroThread5);
        executorService.execute(aeroThread6);
    }

    @Test
    public void function() {
//        System.out.println(Integer.toHexString(171711));
//        System.out.println(Long.toHexString(Double.doubleToLongBits(177777.177777)));

        ParseUtils parseUtils = new ParseUtils();
//        System.out.println(parseUtils.getHexResult("繁"));
//        System.out.println(parseUtils.addZero("abc", 4));
//        System.out.println(parseUtils.convertBigSmall(""));

        String sendStr = "Udp ";//发送的信息
        byte[] sendBuf;//创建字节数组
        sendBuf = sendStr.getBytes();//以字节数组形式发出
        System.out.println(sendBuf.length);
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