package com.evan.wj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.Properties;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class PropertiesTest {
    @Test
    public void test() throws IOException {
        Properties properties = PropertiesLoaderUtils.loadAllProperties("test.properties");
        String name = new String(properties.getProperty("name").getBytes("ISO-8859-1"), "utf-8");
        String age = properties.getProperty("age");
        for (int i = 0; i < 10; i++) {
            System.out.println(name+age);
        }
    }
}