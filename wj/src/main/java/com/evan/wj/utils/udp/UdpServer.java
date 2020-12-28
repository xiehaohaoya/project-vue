package com.evan.wj.utils.udp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * udp服务端
 */
@PropertySource({"classpath:socket.properties"})
@Component
@Slf4j
public class UdpServer {

    @Value("${udp.source.port}")
    private int sourcePort;

    public void udpReceive() {
        DatagramSocket serverSocket;
        try {
            serverSocket = new DatagramSocket(sourcePort);//创建绑定本地指定的端口套接字

            byte[] recBuf = new byte[100];//recBuf为传入数据包的缓冲区
            //接受的是数据包格式
            DatagramPacket receivePacket = new DatagramPacket(recBuf, recBuf.length);

            //TODO 这里可以使用while语句循环接收数据
            serverSocket.receive(receivePacket);//接受数据库包
            //将数据包格式换为String类型用于输出

            String recStr = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
            System.out.println("UdpServer 接收到的数据：" + recStr);//输出

            // 向消息的发送者反馈数据，可以用此实现udp重传反馈
            int port = receivePacket.getPort();//获得到客户端的端口//这是通过客户端的数据包中的获取到客户端的端口
            InetAddress address = receivePacket.getAddress();//得到ip地址
            String sendStr = "UdpServer has received message";//发送的信息
            byte[] sendBuf;//创建字节数组
            sendBuf = sendStr.getBytes();//以字节数组形式发出
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address, port);
            serverSocket.send(sendPacket);//发送
            serverSocket.close();//关闭连接

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}

