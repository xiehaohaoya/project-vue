package com.evan.wj.utils;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端一直启动，客户端关闭后发送数据
 */
public class TcpServer {
    public static void main(String[] args) {
        TCPServer(9999);
    }
    public static void TCPServer(int port){
        Socket socket = null;
        ServerSocket serversocket = null;
        BufferedReader in = null;
        BufferedWriter out = null;
        while (true){
            try {
                //创建连接用于接收信息
                serversocket = new ServerSocket(port);
                System.out.println("启动服务器....");
                socket = serversocket.accept();
                System.out.println("客户端:"+socket.getInetAddress().getLocalHost()+"已连接到服务器");
                //创建使用默认大小的输入缓冲区的缓冲字符输入流。
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //获取信息
                String message=null;
                System.out.println("收到客户端信息:");
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                    //创建使用默认大小的输出缓冲区的缓冲字符输出流。
                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    out.write(message + "\n");
                    out.flush();
                }
            } catch (IOException e) {
                System.out.println("断开连接");
            }finally {
                try {
                    out.close();
                    in.close();
                    socket.close();
                    serversocket.close();
                } catch (IOException e) {
                    System.out.println("断开连接异常");
                }
            }
        }
    }
}
