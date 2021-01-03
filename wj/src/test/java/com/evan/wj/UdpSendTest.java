package com.evan.wj;

import com.evan.wj.listener.ListenerExcludeFilter;
import com.evan.wj.pojo.udp.*;
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

@Slf4j
@SpringBootTest
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@TypeExcludeFilters(ListenerExcludeFilter.class)
public class UdpSendTest {

    //TODO @EnableAutoConfiguration(exclude = BookController.class)有问题

    @Autowired
    UdpClient udpClient;
    @Autowired
    ParseUtils parseUtils;
    @Autowired
    FrameHeaderPojo frameHeaderPojo;
    @Autowired
    FrameTableNumPojo frameTableNumPojo;
    @Autowired
    FrameBodyPojo frameBodyPojo;
    @Autowired
    FramePojo framePojo;
    @Autowired
    ReSendThread reSendThread;
    @Autowired
    ResendKeyPojo resendKeyPojo;

    @Test
    public void function() {

        // 组装帧字段，先将帧校验根据所需字节数设置为n个0，等待帧组装完毕后填补帧校验
        CopyOnWriteArrayList<String> frameFields = new CopyOnWriteArrayList<>();
        frameFields.add("hexString_1");
        frameFields.add("int_4");
        frameFields.add("String_8");
        frameFields.add("double_8");
        frameFields.add("float_4");
        frameFields.add("hexString_2");

        // 组装帧字段值
        CopyOnWriteArrayList<String> frameFieldsValue = new CopyOnWriteArrayList<>();
        frameFieldsValue.add("0E");
        frameFieldsValue.add("1");
        frameFieldsValue.add("xie");
        frameFieldsValue.add("12.11");
        frameFieldsValue.add("13.23");
        frameFieldsValue.add("0000");//帧校验赋初值

        // 组装帧字符串，并发送
        String resultHexStr = parseUtils.getResultHexStr(frameFields, frameFieldsValue, 15);
        udpClient.udpSend(resultHexStr);

        // 组装key，方便重发机制重新发送帧
        resendKeyPojo.setIp("");
        resendKeyPojo.setPort(1);
        resendKeyPojo.setFrameSeq("");
        resendKeyPojo.setResendTimes(1);
        resendKeyPojo.setLastSendTime(1);
        reSendThread.addFrame(resendKeyPojo, resultHexStr);

        // 对帧pojo赋值
//        frameTableNumPojo.setAge(25);
//        frameTableNumPojo.setName("谢man");
//        frameTableNumPojo.setMoney(666.666);
//        frameBodyPojo.setFrameTableNumPojo(frameTableNumPojo);
//        frameHeaderPojo.setFrameNum(1);
//        framePojo.setFrameHeaderPojo(frameHeaderPojo);
//        framePojo.setFrameBodyPojo(frameBodyPojo);
    }
}
