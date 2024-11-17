package com.zhang.codec;

import com.zhang.protocol.MsgHeader;
import com.zhang.protocol.RpcProtocol;
import com.zhang.serialization.RpcSerialization;
import com.zhang.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName RpcEncoder
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 15:49
 * @Version 1.0
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, ByteBuf byteBuf) throws Exception {
        MsgHeader header = msg.getMsgHeader();

        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getSerialization());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getRequestId());

        RpcSerialization rpcSerialization = SerializationFactory.get(header.getMsgType());
        byte[] data = rpcSerialization.serialize(msg.getBody());

        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}
