package com.zhang.handler;

import com.zhang.common.RpcRequestHolder;
import com.zhang.common.RpcServiceHelper;
import com.zhang.model.RpcRequest;
import com.zhang.model.RpcResponse;
import com.zhang.protocol.MsgHeader;
import com.zhang.protocol.MsgStatus;
import com.zhang.protocol.MsgType;
import com.zhang.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @ClassName RpcRequestHandler
 * @Description 服务提供者经过RpcDecoder解码后，再经过RpcRequestHandler进行RPC请求调用
 * SimpleChannelInboundHandler会自动释放资源，ChannelInboundHandlerAdapter需要手动释放
 * @Author zhanggen
 * @Date 2024/11/1 17:06
 * @Version 1.0
 */
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcRequestHandler.class);

    private final Map<String, Object> rpcServiceMap;

    public RpcRequestHandler(Map<String, Object> rpcServiceMap) {
        this.rpcServiceMap = rpcServiceMap;
    }

    // 用于接收到消息后具体的处理逻辑
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> protocol) throws Exception {
        // 由于RPC请求调用耗时可能较长，提交给业务的线程池进行处理
        RpcRequestProcessor.submitRequest(() -> {
            RpcProtocol<RpcResponse> rpcProtocol = new RpcProtocol<>();
            RpcResponse response = new RpcResponse();

            MsgHeader header = protocol.getMsgHeader();
            header.setMsgType((byte) MsgType.RESPONSE.getValue());

            try {
                // 调用RPC服务
                Object result = handle(protocol.getBody());
                response.setData(result);

                header.setStatus((byte) MsgStatus.SUCCESS.getValue());
                rpcProtocol.setMsgHeader(header);
                rpcProtocol.setBody(response);
            } catch (Throwable throwable) {
                LOGGER.error("request {} has unexpected error {}", header.getRequestId(), throwable);
                header.setStatus((byte) MsgStatus.FAILED.getValue());

                response.setMessage(throwable.getMessage());
            }
            // 将请求的结果返回给消费者
            ctx.writeAndFlush(rpcProtocol);
        });
    }

    public Object handle(RpcRequest request) throws InvocationTargetException {
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
        Object serviceBean = rpcServiceMap.get(serviceKey);

        if (serviceBean == null) {
            throw new RuntimeException(String.format("service not exist:%s:%s", request.getClassName(), request.getMethodName()));
        }

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Object[] params = request.getParams();
        Class<?>[] parameterTypes = request.getParameterTypes();

        FastClass fastClass = FastClass.create(serviceClass);
        int index = fastClass.getIndex(methodName, parameterTypes);
        return fastClass.invoke(index, serviceBean, params);
    }
}
