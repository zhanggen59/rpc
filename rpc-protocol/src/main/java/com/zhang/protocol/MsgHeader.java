package com.zhang.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName MsgHeader
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 11:24
 * @Version 1.0
 */
@Data
public class MsgHeader implements Serializable {
    private short magic; // 魔数
    private byte version; // 协议版本号
    private byte serialization; // 序列化算法
    private byte msgType; // 报文类型
    private byte status; // 状态 0:成功；非0：失败
    private long requestId; // 消息 ID
    private int msgLen; // 数据长度
}
