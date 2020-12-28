package com.evan.wj.utils.udp.pojo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 帧体，用来存放表号帧(FrameTableNumPojo)，表号帧可能有多个，可以按需创建Pojo类
 */
@Data
@Component
@Scope("prototype")
public class FrameBodyPojo {
    FrameTableNumPojo frameTableNumPojo;

    public FrameBodyPojo() {}

    public FrameBodyPojo(FrameTableNumPojo frameTableNumPojo) {
        this.frameTableNumPojo = frameTableNumPojo;
    }
}
