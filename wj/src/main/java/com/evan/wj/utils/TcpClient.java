package com.evan.wj.utils;
import java.io.*;
import java.net.Socket;

public class TcpClient {
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 9999;
        String str = "Hello\n" + "世界";
        TCPClient(ip,port,str);
    }
    public static void TCPClient(String ip , int port , String str){
        Socket socket = null;
        BufferedWriter out = null;
        BufferedReader in = null;
        try {
            //创建连接用于发送信息
            socket = new Socket(ip, port);
            //创建使用默认大小的输出缓冲区的缓冲字符输出流。
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(str+"\n");
            out.flush();
            System.out.println("向服务器发送信息:\n"+str);
            //创建使用默认大小的输入缓冲区的缓冲字符输入流。
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //读取数据
            String message=null;
            System.out.println("收到服务器信息:");
            while((message=in.readLine())!=null){
                System.out.println(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("断开连接异常");
            }
        }
    }
}
