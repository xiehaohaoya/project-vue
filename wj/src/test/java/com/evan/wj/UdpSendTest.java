package com.evan.wj;

import com.evan.wj.listener.ListenerExcludeFilter;
import com.evan.wj.pojo.udp.FrameBodyPojo;
import com.evan.wj.pojo.udp.FrameHeaderPojo;
import com.evan.wj.pojo.udp.FramePojo;
import com.evan.wj.pojo.udp.FrameTableNumPojo;
import com.evan.wj.utils.udp.ParseUtils;
import com.evan.wj.utils.udp.UdpClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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

    @Test
    public void function() {
        // 对帧pojo赋值
        frameTableNumPojo.setAge(25);
        frameTableNumPojo.setName("谢man");
        frameTableNumPojo.setMoney(666.666);
        frameBodyPojo.setFrameTableNumPojo(frameTableNumPojo);
        frameHeaderPojo.setFrameNum(1);
        framePojo.setFrameHeaderPojo(frameHeaderPojo);
        framePojo.setFrameBodyPojo(frameBodyPojo);

        // 组装帧字符串
        StringBuffer frameBuffer = new StringBuffer();
        frameBuffer.append("0E");

        /**
         * 处理发送的十六进制字符串，包括补0操作，大小端转换等。
         */
        // 处理int
        frameBuffer.append(
                parseUtils.convertBigSmall(
                        parseUtils.addZero(
                                Integer.toHexString(
                                        framePojo.getFrameBodyPojo().getFrameTableNumPojo().getAge()
                                ), 4
                        )
                )
        );

        // 处理string
        frameBuffer.append(
                parseUtils.getHexResult(
                        framePojo.getFrameBodyPojo().getFrameTableNumPojo().getName()
                )
        );

        // 处理double
        frameBuffer.append(
                parseUtils.convertBigSmall(
                        parseUtils.addZero(
                                Long.toHexString(
                                        Double.doubleToLongBits(
                                                framePojo.getFrameBodyPojo().getFrameTableNumPojo().getMoney()
                                        )
                                ), 4
                        )
                )
        );

        // 处理float
        frameBuffer.append(
                parseUtils.convertBigSmall(
                        parseUtils.addZero(
                                Integer.toHexString(
                                        Float.floatToIntBits(
                                                framePojo.getFrameBodyPojo().getFrameTableNumPojo().getHeight()
                                        )
                                ), 4
                        )
                )
        );

        // 组装帧校验
        frameBuffer.append(parseUtils.makeChecksum(frameBuffer.toString()));

        //TODO 是不是在这里调用重传机制？
        udpClient.udpSend(frameBuffer.toString());
    }
}
