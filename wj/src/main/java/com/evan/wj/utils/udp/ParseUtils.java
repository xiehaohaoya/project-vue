package com.evan.wj.utils.udp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 组装帧和解析帧信息的工具类
 */
@Slf4j
@Component
public class ParseUtils {

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

    /**
     * 对于位数不足的十六进制字符串补0
     *
     * @param str      十六进制字符串
     * @param bytesNum 协议需要的字节数
     * @return 在字符串之前补完0的十六进制字符串
     */
    public String addZero(String str, int bytesNum) {
        int zeroNum = bytesNum * 2 - str.length();
        StringBuffer sb = new StringBuffer();
        while (zeroNum > 0) {
            sb.append("0");
            --zeroNum;
        }
        sb.append(str);
        return sb.toString();
    }

    /**
     * 大小端转换
     *
     * @param str 大端/小端字符串
     * @return 小端/大端字符串
     */
    public String convertBigSmall(String str) {
        if (0 != str.length() % 2) {
            log.error("大小端转换时输入的字符串不是偶数个，没有经过补0操作");
            str = "0" + str;
        }
        if (str.length() <= 0) {
            return str;
        }
        StringBuffer strBuffer = new StringBuffer();
        for (int num = str.length() / 2; num > 0; num--) {
            strBuffer.append(str.charAt(num * 2 - 2));
            strBuffer.append(str.charAt(num * 2 - 1));
        }
        return strBuffer.toString();
    }

    /**
     * 计算校验和
     * @return
     */
    public String makeChecksum(String str) {
        int sum = 0;
        for (int i = 1; i <= str.length()/4; i++) {
            int num = Integer.parseInt(str.substring(4 * i - 4, 4 * i), 16);
            sum += num;
            if(sum>>>16>0) {
                sum = (sum >>> 16) + (sum & 0xffff);
            }
        }
        //取低16位
        return Integer.toHexString(~sum).substring(4);
    }
}