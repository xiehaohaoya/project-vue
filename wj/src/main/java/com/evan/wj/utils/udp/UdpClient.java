package com.evan.wj.utils.udp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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

    public void udpSend() {
        // TODO Auto-generated method stub
        DatagramSocket clientSocket;
        try {
            clientSocket = new DatagramSocket();//创建套接字对象

            //TODO 将帧对象装成字节数组
            String sendStr = "hello i'm client";
            byte[] sendBuf;
            sendBuf = sendStr.getBytes("UTF-8");//String类型转换为字节型

            //以数据包的形式发送出去
            InetAddress address = InetAddress.getByName(destIp);
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address, destPort);
            clientSocket.send(sendPacket);//发送

            byte[] receiveBuf = new byte[100];//存放从服务端接收的字节
            DatagramPacket recPacket = new DatagramPacket(receiveBuf, receiveBuf.length);//指定接受长度的字节
            String recStr = new String(receiveBuf, 0, recPacket.getLength());//再转换成String,用于输出道控制台
            System.out.println("收到" + recStr);//输出到控制台
            clientSocket.close();//关闭
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}