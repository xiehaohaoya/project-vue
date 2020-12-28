package com.evan.wj.utils;

import lombok.Synchronized;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建缓存线程池，线程池通过jvm状态自动创建和销毁线程
 * 使用双重检查锁，保障线程安全和并发度
 */
public class SingletonThreadPool {
    private static ExecutorService executorService = null;

    private SingletonThreadPool() {
    }

    public static synchronized ExecutorService getExecutor() {
        if (executorService == null) {
            synchronized (SingletonThreadPool.class) {
                if (executorService == null) {
                    executorService = Executors.newCachedThreadPool();
                }
            }
        }
        return executorService;
    }
}
