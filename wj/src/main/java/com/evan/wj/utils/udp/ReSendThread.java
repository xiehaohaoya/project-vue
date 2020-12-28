package com.evan.wj.utils.udp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.evan.wj.utils.udp.pojo.FramePojo;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 负责数据包重传的线程
 */
@Slf4j
@Component
public class ReSendThread extends Thread {

    private CopyOnWriteArrayList<FramePojo> framePojoList;

    //TODO 创建一个对FramePojo的封装类，方便重传

    /**
     * 线程的构造方法
     * 线程开启后一直有效
     */
    public ReSendThread() {
        framePojoList = new CopyOnWriteArrayList<>();//实例化队列
    }

    /**
     * 添加重传队列的数据包
     */
    public void addFramePojo(FramePojo framePojo) {
        framePojoList.add(framePojo);
    }

    /**
     * 移除指定编号的数据包
     */
    public void removeFramePojo(int number) {

        //TODO 将foreach改为iterator
        for (int i = 0; i < framePojoList.size(); i++) {
            if (framePojoList.get(i).getFrameHeaderPojo().getFrameNum() == number) {
                framePojoList.remove(i);
            }
        }
    }

    /**
     * 判断重传列表是否为空
     */
    public boolean isEmpty() {
        if (framePojoList.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 执行线程时的方法
     */
    public void run() {
        try {
            while (true) {
                Thread.sleep(100);//刚开始睡眠等待接收
                for (int i = 0; i < framePojoList.size(); i++) {
                    if (framePojoList.get(i) != null) {
                        FramePojo framePojo = framePojoList.get(i);
                        long time = System.currentTimeMillis() - framePojo.getLastTime();//距离上次发送的时间
                        if (time > 300) {
                            //TODO 用udpClient替代这里
//                            sendMethod.sendMessage(framePojo.data, framePojo.destIp,framePojo.destPort);//超出300ms则重发
                            log.info("MainActivity", "重发了一个数据包，它的编号是：" + framePojo.getFrameHeaderPojo().getFrameNum());
                            int reSendTimes = framePojo.getReSendTimes();
                            framePojo.setReSendTimes(++reSendTimes);
                        }
                        if (framePojo.getReSendTimes() >= 3) {     //发送超过三次就移除
                            framePojoList.remove(i);
                        }
                        Thread.sleep(30);//每次数据包处理之间的间隔
                    }
                }
            }
        } catch (Exception e) {
            log.error("MainActivity", "重传线程异常");
            e.printStackTrace();
        }
    }
}
