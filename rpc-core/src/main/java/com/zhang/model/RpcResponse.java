package com.zhang.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RpcResponse
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 11:32
 * @Version 1.0
 */
@Data
public class RpcResponse implements Serializable {
    private Object data; // 请求结果

    private String message; // 错误信息
}
