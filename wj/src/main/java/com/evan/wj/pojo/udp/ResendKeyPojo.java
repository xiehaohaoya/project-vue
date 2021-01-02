package com.evan.wj.pojo.udp;

import lombok.Data;
import org.springframework.context.annotation.Scope;

import java.util.Objects;

@Data
@Scope("prototype")
public class ResendKeyPojo {
    String ip;
    int port;
    String frameSeq;
    int resendTimes;
    long lastSendTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResendKeyPojo that = (ResendKeyPojo) o;
        return lastSendTime == that.lastSendTime &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(port, that.port) &&
                Objects.equals(frameSeq, that.frameSeq) &&
                Objects.equals(resendTimes, that.resendTimes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, frameSeq, resendTimes, lastSendTime);
    }
}
