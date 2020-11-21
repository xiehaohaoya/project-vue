package com.evan.wj.utils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {
    public static void main(String[] args) {
        String url = "http://api.map.baidu.com/direction/v1?mode=driving&origin=清华大学&destination=北京大学&origin_region=北京&destination_region=北京&output=json&ak=您的ak";
        String str = "";
        doPost(url, str);
    }

    public static void doPost(String urlStr,String xmlInfo) {
        BufferedWriter out = null;
        HttpURLConnection conn = null;
        try {
            String str;
            URL url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setConnectTimeout(60 * 1000);
            conn.setReadTimeout(60 * 1000);

            conn.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("logType", "base");

            conn.connect();
            out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            out.write(xmlInfo);
            out.flush();

            System.out.println("urlStr=" + urlStr);
            System.out.println("xmlInfo=" + xmlInfo);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                //得到响应流
                InputStream inputStream = conn.getInputStream();
                //将响应流转换成字符串
                String returnLine = getStringFromInputStream(inputStream);
                str = "Success,返回码为" + responseCode + ",返回信息为" + "---" + returnLine;
            } else {
                str = "Error,返回码为" + responseCode + ",返回信息为" + conn.getResponseMessage();
            }
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                conn.disconnect();
            } catch (IOException e) {
                System.out.println("断开连接异常");
            }
        }
    }
    //获取返回输入流的信息
    private static String getStringFromInputStream(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        is.close();
        // 把流中的数据转换成字符串, 采用的编码是: utf-8
        String status = baos.toString();
        baos.close();
        return status;
    }
}
