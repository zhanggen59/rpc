package com.zhang.api;

import com.zhang.common.RpcRequestHolder;
import com.zhang.model.RpcFuture;
import com.zhang.model.RpcRequest;
import com.zhang.model.RpcResponse;
import com.zhang.protocol.MsgHeader;
import com.zhang.protocol.MsgType;
import com.zhang.protocol.ProtocolConstants;
import com.zhang.protocol.RpcProtocol;
import com.zhang.serialization.SerializationType;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RpcProxy
 * @Description
 * @Author zhanggen
 * @Date 2024/11/3 16:57
 * @Version 1.0
 */
public class RpcProxy implements InvocationHandler {

    private final String serviceVersion;
    private final long timeout;
    private final RegistryService registryService;

    public RpcProxy(String serviceVersion, long timeout, RegistryService registryService) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.registryService = registryService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构建RPC协议对象
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        MsgHeader header = new MsgHeader();
        long requestId = RpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setSerialization((byte) SerializationType.HESSIAN.getValue());
        header.setMsgType((byte) MsgType.REQUEST.getValue());
        header.setStatus((byte) 0x1);
        header.setRequestId(requestId);
        protocol.setMsgHeader(header);

        RpcRequest request = new RpcRequest();
        request.setServiceVersion(serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParams(args);
        request.setParameterTypes(method.getParameterTypes());
        protocol.setBody(request);

        RpcConsumer consumer = new RpcConsumer();
        RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);
        RpcRequestHolder.REQUEST_MAP.put(requestId, future);

        // 发起远程RPC调用
        consumer.sendRequest(protocol, registryService);
        // 等待RPC调用结果
        return future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();
    }
}
