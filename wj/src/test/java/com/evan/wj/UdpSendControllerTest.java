package com.evan.wj;

import com.evan.wj.listener.ListenerExcludeFilter;
import com.evan.wj.pojo.udp.*;
import com.evan.wj.utils.SingletonThreadPool;
import com.evan.wj.utils.udp.ParseUtils;
import com.evan.wj.utils.udp.ReSendThread;
import com.evan.wj.utils.udp.UdpClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

/**
 * 假设有这样一个协议：
 * 帧头
 * TODO 字段名             字段说明                字段类型                字节数
 *      frameType         帧类型                  hexString                1
 *      sourceIp          源ip                    String                  16
 *      sourcePort        源port                  int                     4
 *      sourceHostId      源主机号                 int                     4
 *      destIp            目的ip                 String                   16
 *      destPort          目的port                int                     4
 *      destHostId        目的主机号               int                     4
 *      frameSeq          帧序号                  long                     8
 *      tableNo           帧体表号                 int                     4
 *      tableNum          帧体表数量               long                     8
 *      resendTimes       帧重发次数               byte                     1
 *      ifResponse        帧是否响应帧             byte                     1
 *      checkSum          校验和                  hexString                2
 */

/**
 * 6712表号帧体
 * TODO 字段名                 字段类型           字节数
 *      parseByte              byte              1
 *      parseInt               int               4
 *      parseLong              long              8
 *      parseFloat             float             4
 *      parseDouble            double            8
 *      parseString            String            20
 *      parseHexString         hexString         4
 */

/**
 * 3500表号帧体
 * TODO 字段名                 字段类型           字节数
 *      parseByte              byte              1
 *      parseInt               int               4
 *      parseLong              long              8
 *      parseFloat             float             4
 *      parseDouble            double            8
 *      parseString            String            20
 *      parseHexString         hexString         4
 */

@Slf4j
@SpringBootTest
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@TypeExcludeFilters(ListenerExcludeFilter.class)
public class UdpSendControllerTest {

    //TODO @EnableAutoConfiguration(exclude = BookController.class)有问题

    @Autowired
    UdpClient udpClient;
    @Autowired
    ParseUtils parseUtils;
    @Autowired
    ReSendThread reSendThread;
    @Autowired
    ResendKeyPojo resendKeyPojo;

    @Test
    public void function() {

        // 组装帧字段，先将帧校验根据所需字节数设置为n个0，等待帧组装完毕后填补帧校验
        // byte_1,int_4,float_4,double_8,String_,hexString_
        CopyOnWriteArrayList<String> frameFields = new CopyOnWriteArrayList<>();
        // 放帧头
        frameFields.add("hexString_1");
        frameFields.add("String_16");
        frameFields.add("int_4");
        frameFields.add("int_4");
        frameFields.add("String_16");
        frameFields.add("int_4");
        frameFields.add("int_4");
        frameFields.add("long_8");
        frameFields.add("int_4");
        frameFields.add("long_8");
        frameFields.add("byte_1");
        frameFields.add("byte_1");
        frameFields.add("hexString_2");
        // 放6712表号帧体1
        frameFields.add("byte_1");
        frameFields.add("int_4");
        frameFields.add("long_8");
        frameFields.add("float_4");
        frameFields.add("double_8");
        frameFields.add("String_20");
        frameFields.add("hexString_4");
        // 放6712表号帧体2
        frameFields.add("byte_1");
        frameFields.add("int_4");
        frameFields.add("long_8");
        frameFields.add("float_4");
        frameFields.add("double_8");
        frameFields.add("String_20");
        frameFields.add("hexString_4");

        // 组装帧字段值
        CopyOnWriteArrayList<String> frameFieldsValue = new CopyOnWriteArrayList<>();
        // 组装帧头
        frameFieldsValue.add("BE");
        frameFieldsValue.add("192.168.43.243");
        frameFieldsValue.add("5555");
        frameFieldsValue.add("1");
        frameFieldsValue.add("192.168.43.243");
        frameFieldsValue.add("5555");
        frameFieldsValue.add("1");
        frameFieldsValue.add("1");
        frameFieldsValue.add("6712");
        frameFieldsValue.add("2");
        frameFieldsValue.add("1");
        frameFieldsValue.add("1");
        frameFieldsValue.add("0000");//帧校验赋初值
        // 组装帧体1
        frameFieldsValue.add("1");
        frameFieldsValue.add("256");
        frameFieldsValue.add("671222");
        frameFieldsValue.add("12.11");
        frameFieldsValue.add("123.45567");
        frameFieldsValue.add("hello 谢昊昊");
        frameFieldsValue.add("0AB2");
        // 组装帧体2
        frameFieldsValue.add("3");
        frameFieldsValue.add("236");
        frameFieldsValue.add("234222");
        frameFieldsValue.add("15.13");
        frameFieldsValue.add("547.49267");
        frameFieldsValue.add("say hello 谢昊昊");
        frameFieldsValue.add("92AC");

        // 组装帧字符串，并发送
        String resultHexStr = parseUtils.getResultHexStr(frameFields, frameFieldsValue, 15);
        udpClient.udpSend(resultHexStr);

        // 组装帧的key，将该帧放入重发队列
        resendKeyPojo.setIp("192.168.43.243");
        resendKeyPojo.setPort(5555);
        resendKeyPojo.setFrameSeq(1);
        resendKeyPojo.setResendTimes(1);
        resendKeyPojo.setLastSendTime(System.currentTimeMillis());
        reSendThread.addFrame(resendKeyPojo, resultHexStr);
    }
}
