package com.zhang.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RpcProtocol
 * @Description 自定义通信协议
 * @Author zhanggen
 * @Date 2024/11/1 11:22
 * @Version 1.0
 */
@Data
public class RpcProtocol<T> implements Serializable {
    /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
    +---------------------------------------------------------------+
    | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
    +---------------------------------------------------------------+
    */
    // 消息头
    private MsgHeader msgHeader;

    // 消息体
    private T body;
}
