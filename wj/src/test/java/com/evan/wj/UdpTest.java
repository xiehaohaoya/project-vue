package com.evan.wj;

import com.evan.wj.listener.ListenerExcludeFilter;
import com.evan.wj.utils.udp.UdpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@TypeExcludeFilters(ListenerExcludeFilter.class)
public class UdpTest {

    //TODO @EnableAutoConfiguration(exclude = BookController.class)有问题

    @Autowired
    UdpClient udpClient;
    @Test
    public void function() {
        udpClient.udpSend("qw12");
    }
}
