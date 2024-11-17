package com.zhang.model;

import com.zhang.api.RegistryService;
import com.zhang.api.RpcService;
import com.zhang.codec.RpcDecoder;
import com.zhang.codec.RpcEncoder;
import com.zhang.common.RpcServiceHelper;
import com.zhang.handler.RpcRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RpcProvider
 * @Description
 * @Author zhanggen
 * @Date 2024/10/31 16:00
 * @Version 1.0
 */
public class RpcProvider implements InitializingBean, BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcProvider.class);

    private String serviceAddr;

    private final int servicePort;
    private final RegistryService registryService;

    // 存放服务初始化后对应的bean，缓存作用，在处理RPC请求时可以直接通过rpcServiceMap拿到对应的服务进行调用
    private final Map<String, Object> rpcServiceMap = new HashMap<>();

    public RpcProvider(int servicePort, RegistryService registryService) {
        this.servicePort = servicePort;
        this.registryService = registryService;
    }

    // 在Bean的所有属性被赋值后调用
    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            try {
                startRpcServer();
            } catch (UnknownHostException e) {
                LOGGER.error("start rpc server failed!", e);
            }
        }).start();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 通过注解获取服务元数据信息，构造ServiceMeta对象，并将其发布到注册中心
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);

        if (rpcService != null) {
            String serviceName = rpcService.serviceType().getName();
            String serviceVersion = rpcService.serviceVersion();

            try {
                ServiceMeta serviceMeta = new ServiceMeta();
                serviceMeta.setAddr(serviceAddr);
                serviceMeta.setPort(servicePort);
                serviceMeta.setVersion(serviceVersion);
                serviceMeta.setName(serviceName);

                registryService.registry(serviceMeta);
                rpcServiceMap.put(RpcServiceHelper.buildServiceKey(serviceName, serviceVersion), bean);
            } catch (Exception e) {
                LOGGER.error("failed registry service {}#{}", serviceName, serviceVersion, e);
            }
        }

        return bean;
    }

    public void startRpcServer() throws UnknownHostException {
        this.serviceAddr = InetAddress.getLocalHost().getHostAddress();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcEncoder())
                                    .addLast(new RpcDecoder())
                                    .addLast(new RpcRequestHandler(rpcServiceMap));
                        }
                    });

            ChannelFuture future = bootstrap.bind(this.serviceAddr, this.servicePort).sync();
            LOGGER.info("server addr {} started on port {}", this.serviceAddr, this.servicePort);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
