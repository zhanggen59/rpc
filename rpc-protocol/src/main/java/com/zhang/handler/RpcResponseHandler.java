package com.zhang.handler;

import com.zhang.common.RpcRequestHolder;
import com.zhang.model.RpcFuture;
import com.zhang.model.RpcResponse;
import com.zhang.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @ClassName RpcResponseHandler
 * @Description 对于服务消费者，先解码后再经过RpcResponseHandler，负责响应不同线程的请求结果
 * @Author zhanggen
 * @Date 2024/11/1 17:52
 * @Version 1.0
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> response) throws Exception {
        // 服务消费者发起调用后，维护了requestId和RpcFuture的映射关系,然后为RpcFuture设置响应结果
        long requestId = response.getMsgHeader().getRequestId();
        RpcFuture<RpcResponse> future = RpcRequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(response.getBody());
    }
}
