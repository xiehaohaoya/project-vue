package com.evan.wj.utils.udp;

import com.evan.wj.pojo.udp.ResendKeyPojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 组装帧和解析帧信息的工具类
 */
@Slf4j
@Component
public class ParseUtils {
    private static ConcurrentHashMap<String, String> frameFieldsMap = new ConcurrentHashMap();

    // 在这里添加帧的解析格式
    public ParseUtils() {
        frameFieldsMap.put("frameHeaderFieldsList", "hexString_1,String_16,int_4,int_4,String_16,int_4,int_4,long_8,int_4,long_8,byte_1,byte_1,hexString_2");
        frameFieldsMap.put("headerFieldsNameList", "frameType,sourceIp,sourcePort,sourceHostId,destIp,destPort,destHostId,frameSeq,tableNo,tableNum,resendTimes,ifResponse,checkSum");
        frameFieldsMap.put("frameBodyFieldsList_6712", "byte_1,int_4,long_8,float_4,double_8,String_20,hexString_4");
        frameFieldsMap.put("bodyFieldsNameList_6712", "parseByte,parseInt,parseLong,parseFloat,parseDouble,parseString,parseHexString");
        frameFieldsMap.put("frameBodyFieldsList_3500", "byte_1,int_4,long_8,float_4,double_8,String_20,hexString_4");
        frameFieldsMap.put("bodyFieldsNameList_3500", "parseByte,parseInt,parseLong,parseFloat,parseDouble,parseString,parseHexString");
    }

    @Autowired
    ReSendThread reSendThread;
    @Autowired
    ResendKeyPojo resendKeyPojo;

    /**
     * 将bytes字节数组转普通汉英混合的字符串
     *
     * @param bytes      要解析的字节数组
     * @param startIndex 设置字节数组需要的开始位置
     * @param endIndex   设置字节数组需要的结束位置，截取一段字节数组
     * @return 普通汉英混合的字符串
     */
    public static String bytesToString(byte[] bytes, int startIndex, int endIndex) {
        if (bytes != null && endIndex > 0 && startIndex != endIndex) {
            byte[] byteModel = new byte[endIndex - startIndex];
            System.arraycopy(bytes, startIndex, byteModel, 0, endIndex - startIndex);
            return new String(byteModel, StandardCharsets.UTF_8);
        }
        return "";
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
     * 计算校验和，返回四个字符的16进制字符串
     *
     * @return
     */
    public String makeChecksum(String str) {
        int sum = 0;
        for (int i = 1; i <= str.length() / 4; i++) {
            int num = Integer.parseInt(str.substring(4 * i - 4, 4 * i), 16);
            sum += num;
            if (sum >>> 16 > 0) {
                sum = (sum >>> 16) + (sum & 0xffff);
            }
        }
        //取低16位
        return Integer.toHexString(~sum).substring(4);
    }

    /**
     * 处理发送的十六进制字符串，包括基本类型向16进制转换，补0操作，大小端转换，帧校验计算等。
     *
     * @param frameFields      帧字段的类型以及字节长度
     * @param frameFieldsValue 帧字段的值
     * @param checkSumIndex    帧内存放校验和的位置
     * @return 组装完成的16进制的帧
     */
    public String getResultHexStr(CopyOnWriteArrayList<String> frameFields, CopyOnWriteArrayList<String> frameFieldsValue, int checkSumIndex) {
        // 将帧组装为16进制字符串
        StringBuffer frameBuffer = new StringBuffer();
        // 因为这里不需要修改list的值，所以可以不用iterator，用for遍历
        for (int i = 0; i < frameFields.size(); i++) {
            String[] fields = frameFields.get(i).split("_");
            if ("int".equals(fields[0])) {
                // 处理int
                frameBuffer.append(
                        convertBigSmall(
                                addZero(
                                        Integer.toHexString(Integer.parseInt(frameFieldsValue.get(i))),
                                        Integer.parseInt(fields[1]))));
            } else if ("long".equals(fields[0])) {
                // 处理long
                frameBuffer.append(
                        convertBigSmall(
                                addZero(
                                        Long.toHexString(Long.parseLong(frameFieldsValue.get(i))),
                                        Integer.parseInt(fields[1]))));
            }else if ("String".equals(fields[0])) {
                // 处理string
                frameBuffer.append(
                        addZero(
                                DatatypeConverter.printHexBinary(frameFieldsValue.get(i).getBytes(StandardCharsets.UTF_8)),
                                Integer.parseInt(fields[1])));
            } else if ("double".equals(fields[0])) {
                // 处理double
                frameBuffer.append(
                        convertBigSmall(
                                addZero(
                                        Long.toHexString(Double.doubleToLongBits(Double.parseDouble(frameFieldsValue.get(i)))),
                                        Integer.parseInt(fields[1]))));
            } else if ("float".equals(fields[0])) {
                // 处理float
                frameBuffer.append(
                        convertBigSmall(
                                addZero(
                                        Integer.toHexString(Float.floatToIntBits(Float.parseFloat(frameFieldsValue.get(i)))),
                                        Integer.parseInt(fields[1]))));
            } else if ("hexString".equals(fields[0])) {
                // 处理hexString
                frameBuffer.append(addZero(frameFieldsValue.get(i), Integer.parseInt(fields[1])));
            } else if ("byte".equals(fields[0])) {
                // 处理byte
                byte[] bytes = {0};
                bytes[0] = Byte.parseByte(frameFieldsValue.get(i));
                frameBuffer.append(DatatypeConverter.printHexBinary(bytes));
            }
        }
        // TODO 这里默认checkSum是2个字节，也就是4个字符的16进制字符串
        frameBuffer.replace(checkSumIndex * 2, checkSumIndex * 2 + 4, makeChecksum(frameBuffer.toString()));

        return frameBuffer.toString();
    }

    /**
     * 解析16进制帧字符串，将帧字段对应的值按照协议顺序存放进返回的headerValuesList中
     *
     * @param fieldsList  存放帧的格式，如"int_4_12"或"int_4"，第一个参数是帧字段的类型，第二个参数是帧字段所占的字节数，第三个参数是帧字段在帧内的位置
     * @param hexFrameStr 16进制的帧字符串
     * @return 按照顺序存放的帧字段的list，以及经过切分的16进制字符串
     */
    public CopyOnWriteArrayList<Object> getValuesAndHexStrList(CopyOnWriteArrayList<String> fieldsList, String hexFrameStr) {
        CopyOnWriteArrayList<Object> valuesList = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<Object> resultList = new CopyOnWriteArrayList<>();
        // 解析帧头，将帧头信息按照字段顺序存入list中
        for (int i = 0; i < fieldsList.size(); i++) {
            String type = fieldsList.get(i).split("_")[0];
            int bytesNum = Integer.parseInt(fieldsList.get(i).split("_")[1]);
            if ("int".equals(type)) {
                valuesList.add(Integer.parseInt(convertBigSmall(hexFrameStr.substring(0, 2 * bytesNum)), 16));
                // 解析出帧字段后，就将这部分的帧字符串切除掉
                hexFrameStr = hexFrameStr.substring(2 * bytesNum);
            } else if ("long".equals(type)) {
                valuesList.add(Long.parseLong(convertBigSmall(hexFrameStr.substring(0, 2 * bytesNum)), 16));
                hexFrameStr = hexFrameStr.substring(2 * bytesNum);
            } else if ("String".equals(type)) {
                // 将16进制字符串转为字节数组
                byte[] bytes = DatatypeConverter.parseHexBinary(hexFrameStr.substring(0, 2 * bytesNum));
                // 将bytes字节数组转普通汉英混合的字符串
                valuesList.add(bytesToString(bytes, 0, bytes.length));
                hexFrameStr = hexFrameStr.substring(2 * bytesNum);
            } else if ("double".equals(type)) {
                valuesList.add(Double.longBitsToDouble(Long.parseLong(convertBigSmall(hexFrameStr.substring(0, 2 * bytesNum)), 16)));
                hexFrameStr = hexFrameStr.substring(2 * bytesNum);
            } else if ("float".equals(type)) {
                valuesList.add(Float.intBitsToFloat(Integer.parseInt(convertBigSmall(hexFrameStr.substring(0, 2 * bytesNum)), 16)));
                hexFrameStr = hexFrameStr.substring(2 * bytesNum);
            } else if ("hexString".equals(type)) {
                valuesList.add(hexFrameStr.substring(0, 2 * bytesNum));
                hexFrameStr = hexFrameStr.substring(2 * bytesNum);
            } else if ("byte".equals(type)) {
                byte[] bytes = DatatypeConverter.parseHexBinary(hexFrameStr.substring(0, 2 * bytesNum));
                valuesList.add(bytes[0]);
                hexFrameStr = hexFrameStr.substring(2 * bytesNum);
            }
        }
        resultList.add(valuesList);
        resultList.add(hexFrameStr);
        return resultList;
    }

    /**
     * 解析接收到的16进制字符串
     *
     * @param hexFrameStr
     * @return
     */
    public String parseFrame(String hexFrameStr, int checkSumIndex) {
        // frameHeaderFieldsList用来存放帧头的字段格式，如"int_4_2"或"int_4"
        String[] frameHeaderFieldsArr = frameFieldsMap.get("frameHeaderFieldsList").split(",");
        CopyOnWriteArrayList<String> frameHeaderFieldsList = new CopyOnWriteArrayList<>(Arrays.asList(frameHeaderFieldsArr));

        // headerFieldsNameList用来存放帧头的字段名字，如"tableNum"
        String[] headerFieldsNameArr = frameFieldsMap.get("headerFieldsNameList").split(",");
        CopyOnWriteArrayList<String> headerFieldsNameList = new CopyOnWriteArrayList<>(Arrays.asList(headerFieldsNameArr));

        // headerValuesList用来存放帧头字段的值
        CopyOnWriteArrayList headerValuesList = new CopyOnWriteArrayList();

        // 判断帧类型是否正确，不正确则返回null，在上层丢弃
        // TODO 这里按需修改帧类型
        if (!"BE".equals(hexFrameStr.substring(0,2))) {
            return null;
        }
        // 判断校验和是否正确，不正确则返回null，在上层丢弃
        // TODO 这里按需修改checkSum字节长度"0000"
        String checkSum = makeChecksum(hexFrameStr.substring(0, checkSumIndex * 2) + "0000" + hexFrameStr.substring(checkSumIndex * 2 + 4));
        if (!checkSum.equals(hexFrameStr.substring(checkSumIndex * 2, checkSumIndex * 2 + 4))) {
            return null;
        }

        // 解析帧头，将帧头字段的值按照顺序存入headerValuesList中
        CopyOnWriteArrayList<Object> valuesAndHexStrList;
        valuesAndHexStrList = getValuesAndHexStrList(frameHeaderFieldsList, hexFrameStr);
        headerValuesList = (CopyOnWriteArrayList) valuesAndHexStrList.get(0);
        // 返回hexFrameStr为切去帧头的16进制字符串
        hexFrameStr = valuesAndHexStrList.get(1).toString();

        // 组装resendKeyPojo对象，并判断是否需要响应
        for (int i = 0; i < headerFieldsNameList.size(); i++) {
            if ("sourceIp".equals(headerFieldsNameList.get(i))) {
                resendKeyPojo.setIp(headerValuesList.get(i).toString());
            } else if ("sourcePort".equals(headerFieldsNameList.get(i))) {
                resendKeyPojo.setPort(Integer.parseInt(headerValuesList.get(i).toString()));
            } else if ("frameSeq".equals(headerFieldsNameList.get(i))) {
                resendKeyPojo.setFrameSeq(Long.parseLong(headerValuesList.get(i).toString()));
            } else if ("ifResponse".equals(headerFieldsNameList.get(i))) {
                // 判断该帧是否是返回的响应帧，如果是响应帧，取消该帧的重传机制，返回null，在上层丢弃
                // TODO 这里暂且先默认1是响应帧
                if (1 == Integer.parseInt(headerValuesList.get(i).toString())) {
                    reSendThread.removeFrame(resendKeyPojo);
                    return null;
                }
            }
        }

        // 如果不是响应帧，需要解析出帧信息，判断有几个帧体，并返回响应
        long tableNum = 0;
        int tableNo;
        // 存放帧字段结构的list，如"int_4_12"或"int_4"
        CopyOnWriteArrayList<String> frameBodyFieldsList = new CopyOnWriteArrayList<>();
        // 存放帧字段名字的list
        CopyOnWriteArrayList<String> bodyFieldsNameList = new CopyOnWriteArrayList();
        for (int i = 0; i < headerFieldsNameList.size(); i++) {
            // 得到帧体的表号
            if ("tableNo".equals(headerFieldsNameList.get(i))) {
                tableNo = Integer.parseInt(headerValuesList.get(i).toString());
                // 匹配到tableNum，为帧字段结构frameBodyFieldsList赋值
                String frameBodyFieldsListStr = frameFieldsMap.get("frameBodyFieldsList_" + tableNo);
                String[] frameBodyFieldsArr = frameBodyFieldsListStr.split(",");
                for (String frameBodyField : frameBodyFieldsArr) {
                    frameBodyFieldsList.add(frameBodyField);
                }
                // 匹配到tableNum，为帧字段名字的bodyFieldsNameList赋值
                String bodyFieldsNameListStr = frameFieldsMap.get("bodyFieldsNameList_" + tableNo);
                String[] bodyFieldsNameArr = bodyFieldsNameListStr.split(",");
                for (String bodyFieldName : bodyFieldsNameArr) {
                    bodyFieldsNameList.add(bodyFieldName);
                }
            }
        }
        for (int i = 0; i < headerFieldsNameList.size(); i++) {
            // 得到帧体的数量
            if ("tableNum".equals(headerFieldsNameList.get(i))) {
                tableNum = Integer.parseInt(headerValuesList.get(i).toString());
            }
        }
        // 用multiBodyValuesListList存放多个帧体内字段的数据
        CopyOnWriteArrayList<CopyOnWriteArrayList> multiBodyValuesListList = new CopyOnWriteArrayList();
        for (int i = 0; i < tableNum; i++) {
            CopyOnWriteArrayList bodyValuesAndHexStrList = getValuesAndHexStrList(frameBodyFieldsList, hexFrameStr);
            CopyOnWriteArrayList bodyValuesList = (CopyOnWriteArrayList) bodyValuesAndHexStrList.get(0);
            multiBodyValuesListList.add(bodyValuesList);
            hexFrameStr = bodyValuesAndHexStrList.get(1).toString();
        }

        // TODO 将解析的帧的有用数据通过webSocket发送到前台，数据已经放在了headerValuesList+multiBodyValuesListList中
        log.info("帧头信息:{}", headerValuesList.toString());
        log.info("帧体信息:{}", multiBodyValuesListList.toString());

        // TODO 组装响应帧
        CopyOnWriteArrayList<String> responseFrameFields = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<String> responseFrameValues = new CopyOnWriteArrayList<>();
//        String responseHexStr = getResultHexStr(responseFrameFields, responseFrameValues, checkSumIndex);
        String responseHexStr = "hello xie ya xie hao hao";

        return responseHexStr;
    }
}