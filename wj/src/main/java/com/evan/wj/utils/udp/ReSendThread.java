package com.evan.wj.utils.udp;

import com.evan.wj.pojo.udp.ResendKeyPojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负责数据包重传的线程
 */
@Slf4j
@Component
public class ReSendThread extends Thread {

    private volatile ConcurrentHashMap<ResendKeyPojo, String> resendHashMap;

    @Autowired
    UdpClient udpClient;
    @Autowired
    ParseUtils parseUtils;

    /**
     * 线程的构造方法
     * 线程开启后一直有效
     */
    public ReSendThread() {
        resendHashMap = new ConcurrentHashMap<>();//实例化队列
    }

    /**
     * 添加重传队列的数据包
     */
    public void addFrame(ResendKeyPojo resendKeyPojo, String resendHexStr) {
        resendHashMap.put(resendKeyPojo, resendHexStr);
    }

    /**
     * 移除指定编号的数据包
     */
    public void removeFrame(ResendKeyPojo resendKeyPojo) {
        //ConcurrentHashMap的remove方法如果没有key，则返回null，不会报异常
        resendHashMap.remove(resendKeyPojo);
    }

    /**
     * 执行线程时的方法
     */
    public void run() {
        try {
            while (true) {
                if (!resendHashMap.isEmpty()) {
                    Iterator iterator = resendHashMap.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry entry = (Map.Entry) iterator.next();
                        ResendKeyPojo resendKeyPojo = (ResendKeyPojo)entry.getKey();
                        String resendHexStr = (String)entry.getValue();

                        long time = System.currentTimeMillis() - resendKeyPojo.getLastSendTime();//距离上次发送的时间
                        if (resendKeyPojo.getResendTimes() == 1 && time > 300) {   //超出300ms则重发
                            String resendTimes = parseUtils.convertBigSmall(
                                    parseUtils.addZero(
                                            Integer.toHexString(
                                                    resendKeyPojo.getResendTimes() + 1
                                            ), 4
                                    )
                            );

                            // 重新组装帧数据，并发送
                            resendHexStr = resendHexStr.substring(0, 12) + resendTimes + resendHexStr.substring(20, 24);
                            String checkSum = parseUtils.makeChecksum(resendHexStr);
                            resendHexStr = resendHexStr + checkSum;
                            udpClient.udpSend(resendHexStr, resendKeyPojo.getIp(), resendKeyPojo.getPort());
                            log.info("MainActivity", "重发了一个数据包，它的编号是：" + resendKeyPojo.getFrameSeq());

                            int reSendTimes = resendKeyPojo.getResendTimes();
                            resendKeyPojo.setResendTimes(++reSendTimes);
                            resendKeyPojo.setLastSendTime(System.currentTimeMillis());
                        }else if (resendKeyPojo.getResendTimes() == 2 && time > 200) {   //超出300ms则重发
                            String resendTimes = parseUtils.convertBigSmall(
                                    parseUtils.addZero(
                                            Integer.toHexString(
                                                    resendKeyPojo.getResendTimes() + 1
                                            ), 4
                                    )
                            );

                            // 重新组装帧数据，并发送
                            resendHexStr = resendHexStr.substring(0, 12) + resendTimes + resendHexStr.substring(20, 24);
                            String checkSum = parseUtils.makeChecksum(resendHexStr);
                            resendHexStr = resendHexStr + checkSum;
                            udpClient.udpSend(resendHexStr, resendKeyPojo.getIp(), resendKeyPojo.getPort());
                            log.info("MainActivity", "重发了一个数据包，它的编号是：" + resendKeyPojo.getFrameSeq());

                            int reSendTimes = resendKeyPojo.getResendTimes();
                            resendKeyPojo.setResendTimes(++reSendTimes);
                            resendKeyPojo.setLastSendTime(System.currentTimeMillis());
                        }else if (resendKeyPojo.getResendTimes() == 3 && time > 100) {   //超出300ms则重发
                            String resendTimes = parseUtils.convertBigSmall(
                                    parseUtils.addZero(
                                            Integer.toHexString(
                                                    resendKeyPojo.getResendTimes() + 1
                                            ), 4
                                    )
                            );

                            // 重新组装帧数据，并发送
                            resendHexStr = resendHexStr.substring(0, 12) + resendTimes + resendHexStr.substring(20, 24);
                            String checkSum = parseUtils.makeChecksum(resendHexStr);
                            resendHexStr = resendHexStr + checkSum;
                            udpClient.udpSend(resendHexStr, resendKeyPojo.getIp(), resendKeyPojo.getPort());
                            log.info("MainActivity", "重发了一个数据包，它的编号是：" + resendKeyPojo.getFrameSeq());

                            int reSendTimes = resendKeyPojo.getResendTimes();
                            resendKeyPojo.setResendTimes(++reSendTimes);
                            resendKeyPojo.setLastSendTime(System.currentTimeMillis());
                        }
                        if (resendKeyPojo.getResendTimes() >= 3) {  //发送超过三次就移除
                            resendHashMap.remove(resendKeyPojo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("MainActivity", "重传线程异常");
            e.printStackTrace();
        }
    }
}