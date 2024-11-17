package com.zhang.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RpcRequest
 * @Description RPC请求，包含必要的参数
 * @Author zhanggen
 * @Date 2024/11/1 11:26
 * @Version 1.0
 */
@Data
public class RpcRequest implements Serializable {
    private String serviceVersion; // 服务版本

    private String className; // 服务接口名

    private String methodName; // 服务方法名

    private Object[] params; // 方法参数列表

    private Class<?>[] parameterTypes; // 方法参数类型列表
}
