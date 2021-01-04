package com.evan.wj;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

public class ReplaceDemo {
    private final String hexString = "0123456789ABCDEF";

    /**
     * 将一个汉字转为十六进制
     *
     * @param data 一个汉字
     * @return 十六进制字符串
     */
    public String encodeCN(String data) {
        byte[] bytes;
        bytes = data.getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        for (byte aByte : bytes) {
            sb.append(hexString.charAt((aByte & 0xf0) >> 4));
            sb.append(hexString.charAt((aByte & 0x0f) >> 0));
        }
        return sb.toString();
    }

    /**
     * 将一个英语字符转为十六进制
     *
     * @param data 一个英文字符的字符串
     * @return 十六进制字符串
     */
    public String encodeStr(String data) {
        StringBuffer result = new StringBuffer();
        byte[] bytes;
        bytes = data.getBytes(StandardCharsets.UTF_8);
        for (byte aByte : bytes) {
            result.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    /**
     * 判定是否为中文汉字
     *
     * @param data
     * @return true为汉字，false为英文
     */
    public boolean isCN(String data) {
        boolean flag = false;
        String regex = "^[\u4e00-\u9fa5]*$";
        if (data.matches(regex)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 将字符串转为十六进制字符串
     *
     * @param targetStr 字符串
     * @return 十六进制字符串
     */
    public String getHexResult(String targetStr) {
        StringBuilder hexStr = new StringBuilder();
        int len = targetStr.length();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                char tempStr = targetStr.charAt(i);
                String data = String.valueOf(tempStr);
                if (isCN(data)) {
                    hexStr.append(encodeCN(data));
                } else {
                    hexStr.append(encodeStr(data));
                }
            }
        }
        return hexStr.toString();
    }

    public String bytesToString(byte[] bytes,int startIndex,int endIndex){
        if(bytes!=null && endIndex>0 && startIndex!=endIndex){
            byte[] byteModel = new byte[endIndex-startIndex];
            System.arraycopy(bytes, startIndex, byteModel, 0, endIndex-startIndex);
            return new String(byteModel, StandardCharsets.UTF_8);
        }
        return "";
    }

    @Test
    public void function() {
//        String str = "a你";
//        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
//        System.out.println(DatatypeConverter.printHexBinary(bytes));
//        System.out.println(getHexResult(str));
//
//        byte[] bytes1 = DatatypeConverter.parseHexBinary("61E4BDA0");
//        System.out.println("");

    }
}
