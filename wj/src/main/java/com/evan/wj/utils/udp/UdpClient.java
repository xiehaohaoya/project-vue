package com.evan.wj.utils.udp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * udp客户端
 */
@PropertySource({"classpath:socket.properties"})
@Component
@Slf4j
public class UdpClient {
    @Value("${udp.dest.ip}")
    private String destIp;

    @Value("${udp.dest.port}")
    private int destPort;

    @Autowired
    ReSendThread reSendThread;

    /**
     * udp发送数据
     *
     * @param sendStr 要发送的十六进制字符串
     */
    public void udpSend(String sendStr) {
        log.info("clientSocket向{}:{}发送:String-{}", destIp, destPort, sendStr);
        DatagramSocket clientSocket;
        String recStr = null;
        try {
            clientSocket = new DatagramSocket();//创建套接字对象
            byte[] sendBuf = sendStr.getBytes(StandardCharsets.UTF_8);//String类型转换为字节型

            //以数据包的形式发送出去
            InetAddress address = InetAddress.getByName(destIp);
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address, destPort);
            clientSocket.send(sendPacket);//发送
            log.info("clientSocket向{}:{}发送:Bytes-{}", destIp, destPort, sendBuf);

            /*
            //解开这里可以直接接收响应的数据，这里涨知识，不需要使用
            Thread.sleep(10000);
            byte[] receiveBuf = new byte[4096];//存放从服务端接收的字节
            DatagramPacket recPacket = new DatagramPacket(receiveBuf, receiveBuf.length);//指定接受长度的字节
            clientSocket.receive(recPacket);
            recStr = new String(receiveBuf, 0, recPacket.getLength());//再转换成String,用于输出道控制台
            log.info("clientSocket:本机收到:{}", recStr);
            */

            clientSocket.close();//关闭
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重载udp发送数据方法，用ReSendThread重发的时候，需要使用该方法
     *
     * @param sendStr 要发送的十六进制字符串
     */
    public void udpSend(String sendStr, String destIp, int destPort) {
        log.info("clientSocket向{}:{}发送:String-{}", destIp, destPort, sendStr);
        DatagramSocket clientSocket;
        String recStr = null;
        try {
            clientSocket = new DatagramSocket();//创建套接字对象
            byte[] sendBuf = sendStr.getBytes(StandardCharsets.UTF_8);//String类型转换为字节型

            //以数据包的形式发送出去
            InetAddress address = InetAddress.getByName(destIp);
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address, destPort);
            clientSocket.send(sendPacket);//发送
            log.info("clientSocket向{}:{}发送:Bytes-{}", destIp, destPort, sendBuf);

            clientSocket.close();//关闭
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}