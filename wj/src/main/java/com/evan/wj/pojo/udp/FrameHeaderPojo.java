package com.evan.wj.pojo.udp;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 帧头，用来存放帧头信息
 */
@Data
@Component
@Scope("prototype")
public class FrameHeaderPojo {
}
