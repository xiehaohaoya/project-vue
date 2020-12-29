package com.evan.wj.pojo.udp;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 表号帧，用来存放表号信息
 */
@Data
@Component
@Scope("prototype")
public class FrameTableNumPojo {
    int age;
    String name;
    double money;
    float height;
}
