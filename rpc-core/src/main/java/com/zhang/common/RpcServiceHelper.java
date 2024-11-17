package com.zhang.common;

/**
 * @ClassName RpcServiceHelper
 * @Description
 * @Author zhanggen
 * @Date 2024/10/31 17:06
 * @Version 1.0
 */
public class RpcServiceHelper {

    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.join("#", serviceName, serviceVersion);
    }
}
