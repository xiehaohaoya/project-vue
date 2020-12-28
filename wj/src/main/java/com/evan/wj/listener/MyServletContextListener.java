package com.evan.wj.listener;

import com.evan.wj.pojo.User;
import com.evan.wj.server.UserService;
import com.evan.wj.utils.HbaseUtils;
import com.evan.wj.utils.MyFileUtils;
import com.evan.wj.utils.RedisUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 使用 ApplicationListener 来初始化一些数据到 application 域中的监听器
 * 监听器：在程序启动的时候运行
 * @author shengni ni
 * @date 2018/07/05
 */
@Slf4j
public class MyServletContextListener implements ApplicationListener<ContextRefreshedEvent> {

//    @Autowired
//    private HbaseUtils hbaseUtils;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 先获取到 application 上下文
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        // 获取对应的 service
        UserService userService = applicationContext.getBean(UserService.class);
        User user = userService.selectUserByName("谢昊昊");
        // 获取 application 域对象，将查到的信息放到 application 域中
        ServletContext application = applicationContext.getBean(ServletContext.class);
        application.setAttribute("user", user);

        // 代码测试
        this.fileFuncTest();
    }

    /**
     * MyFileUtils工具类代码测试
     */
    public void fileFuncTest() throws Exception{
/*
        // 读取xml配置文件
        new MyFileUtils().readXml("demo.xml");

        // 获取配置文件绝对路径
        System.out.println(new MyFileUtils().getAbsolutionPath("test.properties"));

        // 解压文件
        new MyFileUtils().ZipCompress("D:\\test","D:\\test.zip");

        // 解压文件
        new MyFileUtils().zipUncompress("D:\\zip.zip","D:\\unZip");

        // 遍历文件和文件夹
        new MyFileUtils().getAllFile(new File("D:\\unZip"),true,new ArrayList<String>());

        // file转multipartFile
        MultipartFile multipartFile = new MyFileUtils().fileToMultipartFile(new File("D:\\test\\1.txt"));

        // multipartFile转file
        File file = new MyFileUtils().multipartFileToFile(multipartFile,"D:\\test\\2.txt");

        // Java代码删除文件夹及其子文件夹和文件操作
        FileUtils.deleteDirectory(new File("D:\\unZip"));//tmp目录下面包含多个子文件夹和文件

        // jar包打包方式读取配置文件
        new MyFileUtils().readJarProperties("test.properties");

        // 用spring的包读取properties文件避免中文乱码
        new MyFileUtils().readByUTF8("test.properties");

        // 按行读取文件，返回字符串
        new MyFileUtils().readFileByLines("G:\\project-vue\\wj\\src\\main\\resources\\test.json");

        // 解析json字符串
        new MyFileUtils().readJsonFile(new MyFileUtils().readFileByLines("G:\\project-vue\\wj\\src\\main\\resources\\test.json"));

        // 判断文件是否为压缩文件
        System.out.println(new MyFileUtils().isArchiveFile(new File("D:\\test\\1.txt")));

        // 正则表达式，判断是否为一个或多个空格符，制表符
        System.out.println(new MyFileUtils().regexPattern("  ","\\s+"));



        // redis存放数据测试
        new RedisUtils().redisSetVal();

        // redis存放数据测试
        new RedisUtils().redisGetVal();
*/

//        initTable();
    }

//    public void initTable(){
//        hbaseUtils.initRepository();
//        Map<String, List<String>> tableMap = new HashMap<>();
//        tableMap.put("default:actionFlow",Arrays.asList("info","logs"));
//        tableMap.put("default:recording",Arrays.asList("info","logs"));
//        hbaseUtils.createManyTable(tableMap);
//    }

}