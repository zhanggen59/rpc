package com.zhang.api;

import com.zhang.codec.RpcDecoder;
import com.zhang.codec.RpcEncoder;
import com.zhang.common.RpcRequestHolder;
import com.zhang.common.RpcServiceHelper;
import com.zhang.handler.RpcResponseHandler;
import com.zhang.model.RpcConstant;
import com.zhang.model.RpcRequest;
import com.zhang.model.ServiceMeta;
import com.zhang.protocol.RpcProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName RpcConsumer
 * @Description
 * @Author zhanggen
 * @Date 2024/11/4 9:09
 * @Version 1.0
 */
public class RpcConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsumer.class);

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public RpcConsumer() {
        this.bootstrap = new Bootstrap();
        this.eventLoopGroup = new NioEventLoopGroup(4);

        bootstrap.group(eventLoopGroup)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RpcEncoder())
                                .addLast(new RpcDecoder())
                                .addLast(new RpcResponseHandler());
                    }
                });
    }

    public void sendRequest(RpcProtocol<RpcRequest> protocol, RegistryService registryService) throws Exception {
        RpcRequest request = protocol.getBody();
        Object[] params = request.getParams();

        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
        int invokeHashCode = params.length > 0 ? params[0].hashCode() : serviceKey.hashCode();

        // 找到最合适的服务节点
        ServiceMeta serviceMeta = registryService.discovery(serviceKey, invokeHashCode);

        if (serviceMeta != null) {
            ChannelFuture future = bootstrap.connect(serviceMeta.getAddr(), serviceMeta.getPort()).sync();
            future.addListener((ChannelFutureListener) arg0 -> {
                if (future.isSuccess()) {
                    LOGGER.info("connect rpc server {} port {} success", serviceMeta.getAddr(), serviceMeta.getPort());
                } else {
                    LOGGER.error("connect rpc server {} port {} failed", serviceMeta.getAddr(), serviceMeta.getPort());
                    future.cause().printStackTrace();
                    eventLoopGroup.shutdownGracefully();
                }
            });

            future.channel().writeAndFlush(protocol);
        }
    }
}
