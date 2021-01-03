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
 * udp服务端
 */
@PropertySource({"classpath:socket.properties"})
@Component
@Slf4j
public class UdpServer {

    @Value("${udp.source.port}")
    private int sourcePort;

    @Autowired
    ParseUtils parseUtils;

    public void udpReceive() {
        DatagramSocket serverSocket;
        try {
            serverSocket = new DatagramSocket(sourcePort);//创建绑定本地指定的端口套接字
            byte[] recBuf = new byte[4096];//recBuf为传入数据包的缓冲区
            //接受的是数据包格式
            DatagramPacket receivePacket = new DatagramPacket(recBuf, recBuf.length);
            //使用while语句循环接收数据
            while (true){
                serverSocket.receive(receivePacket);//接受数据库包
                //将数据包格式换为String类型用于输出

                String recHexStr = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
                log.info("UdpServer 接收到的数据:{}",recHexStr);//输出

                // 解析接受的数据，返回需要响应的数据
                String responseHexStr = parseUtils.parseFrame(recHexStr);

                // 如果返回的是null，说明不需要响应，直接开始下一轮接收
                if(responseHexStr == null){
                    continue;
                }else {
                    // 向消息的发送者发送响应数据
                    int port = receivePacket.getPort();//获得到客户端的端口//这是通过客户端的数据包中的获取到客户端的端口
                    InetAddress address = receivePacket.getAddress();//得到ip地址

                    byte[] responseBuf;//创建字节数组
                    responseBuf = responseHexStr.getBytes();//以字节数组形式发出
                    DatagramPacket sendPacket = new DatagramPacket(responseBuf, responseBuf.length, address, port);
                    serverSocket.send(sendPacket);//发送
                    log.info("UdpServer响应:发送至{}:{},数据:{}",address.getHostName(),port,responseHexStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}