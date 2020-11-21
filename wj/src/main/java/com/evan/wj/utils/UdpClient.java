package com.evan.wj.utils;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/**
 * udp客户端
 */
public class UdpClient {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        DatagramSocket clientsocket;
        try {

            clientsocket=new DatagramSocket();//创建套接字对象
            String sendStr="hello i'm client";
            byte[] sendBuf;
            sendBuf=sendStr.getBytes();//String类型转换为字节型
            InetAddress addr =InetAddress.getByName("192.168.8.198");
            int port =1234;//端口
            //以数据包的形式发送出去
            DatagramPacket sendPacket=new DatagramPacket(sendBuf, sendBuf.length,addr,port);
            clientsocket.send(sendPacket);//发送

            byte[] recvBuf=new byte[100];//存放从服务端接收的字节
            DatagramPacket recPacket=new DatagramPacket(recvBuf, recvBuf.length);//指定接受长度的字节
            String recStr =new String(recvBuf,0,recPacket.getLength());//再转换成String,用于输出道控制台
            System.out.println("收到"+recStr);//输出到控制台
            clientsocket.close();//关闭

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}

