package com.evan.wj;

import com.evan.wj.utils.MyKafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 先启动spring boot，再启动该测试类。
 * 在测试类的console中发送message，查看日志打印信息
 * 在spring boot的启动console中接收message，查看日志打印信息
 */
@Slf4j
@SpringBootTest
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class KafkaTest {

    @Autowired
    MyKafkaUtils myKafkaUtils;

    @Test
    public void producerSend() {
        for (int num = 1; num < 10; num++) {
            myKafkaUtils.producerSend("------kafka测试发送第"+num+"次");
        }
    }
}