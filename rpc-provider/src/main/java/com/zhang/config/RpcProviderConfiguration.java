package com.zhang.config;

import com.zhang.api.RegistryFactory;
import com.zhang.api.RegistryService;
import com.zhang.api.RegistryType;
import com.zhang.model.RpcProperties;
import com.zhang.model.RpcProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @ClassName RpcProviderConfiguration
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 9:39
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(RpcProperties.class)
public class RpcProviderConfiguration {

    @Resource
    private RpcProperties rpcProperties;

    @Bean
    public RpcProvider init() throws Exception {
        String registryType = rpcProperties.getRegistryType();
        if (registryType == null) {
            throw new IllegalArgumentException("registryType is null");
        }
        RegistryService registryService = RegistryFactory.getInstance(rpcProperties.getRegistryAddr(), RegistryType.valueOf(registryType));
        return new RpcProvider(rpcProperties.getServicePort(), registryService);
    }
}
