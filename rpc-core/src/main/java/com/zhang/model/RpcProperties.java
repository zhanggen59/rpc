package com.zhang.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName RpcProperties
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 9:11
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "rpc")
public class RpcProperties {
    private int servicePort;

    private String registryAddr;

    private String registryType;
}
