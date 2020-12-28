package com.evan.wj;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 环境：
 * 1.linux文件系统搭建的单机hbase
 * 2.javaApi连接linux的hbase
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class test {

//    @Test
//    public void test1() throws IOException {
//
//        //1、创建一个configuration对象
//        Configuration conf = HBaseConfiguration.create();
//
//        //2、设置hbase连接参数
//        conf.set("hbase.zookeeper.quorum", "192.168.43.86");
//        conf.set("hbase.zookeeper.property.clientPort", "2181");
//        // 设置超时时间限制
//        conf.set("hbase.client.pause", "60");
//        conf.set("hbase.client.retries.number", "3");
//        conf.set("hbase.rpc.timeout", "2000");
//        conf.set("hbase.client.operation.timeout", "3000");
//        conf.set("hbase.client.scanner.timeout.period", "10000");
//
//        //3.创建线程池
//        ExecutorService pool = Executors.newFixedThreadPool(50);
//
//        //4、获取一个连接对象Connection
//        Connection conn = ConnectionFactory.createConnection(conf, pool);
//
//        //5、获取Admin对象
//        Admin admin = conn.getAdmin();
//        System.out.println("获取到admin");
//
//        //6.查询hbase中是否有student表
//        boolean student = admin.tableExists(TableName.valueOf("student"));
//        System.out.println("是否有hbase表：" + student);
//
//        //6、释放资源
//        admin.close();
//        conn.close();
//    }

    @Test
    public void test2() {
        for (int i = 0; i < 10; i++) {
            System.out.println("aaa");
        }
    }

}