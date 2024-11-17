package com.zhang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * rpc-provider主要完成四个核心流程：
 * 1. 服务提供者启动服务，并暴露服务端口
 * 2. 启动时扫描需要对外发布的服务，将服务信息注册到注册中心
 * 3. 接收RPC请求，解码后得到请求信息
 * 4. 提交请求至自定义线程池处理，并将处理结果写回客户端
 */
@SpringBootApplication
@EnableConfigurationProperties
public class RpcProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(RpcProviderApplication.class, args);
    }
}
