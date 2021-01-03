package com.evan.wj.pojo.udp;

import lombok.Data;
import org.springframework.context.annotation.Scope;

import java.util.Objects;

@Data
@Scope("prototype")
public class ResendKeyPojo {

    // 目标ip
    String ip;
    // 目标端口
    int port;
    // 帧序号
    long frameSeq;
    // 重传次数
    int resendTimes;
    // 上一次发送该帧的时间
    long lastSendTime;

    /**
     * ip，port，frameSeq相同则被认为是同一个帧，不需要考虑resendTimes和lastSendTime
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResendKeyPojo that = (ResendKeyPojo) o;
        return lastSendTime == that.lastSendTime &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, frameSeq);
    }
}