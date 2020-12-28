package com.evan.wj.utils.udp.pojo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 帧，存放帧头(FrameHeaderPojo)和帧体(FrameBodyPojo)
 */
@Data
@Component
@Scope("prototype")
public class FramePojo {

    long lastTime;
    int reSendTimes;

    FrameHeaderPojo frameHeaderPojo;
    FrameBodyPojo frameBodyPojo;

    public FramePojo() {}

    public FramePojo(FrameHeaderPojo frameHeaderPojo, FrameBodyPojo frameBodyPojo) {
        this.frameHeaderPojo = frameHeaderPojo;
        this.frameBodyPojo = frameBodyPojo;
    }
}
