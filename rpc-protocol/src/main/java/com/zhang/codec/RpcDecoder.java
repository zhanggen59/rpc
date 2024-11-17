package com.zhang.codec;

import com.zhang.model.RpcRequest;
import com.zhang.model.RpcResponse;
import com.zhang.protocol.MsgHeader;
import com.zhang.protocol.MsgType;
import com.zhang.protocol.ProtocolConstants;
import com.zhang.protocol.RpcProtocol;
import com.zhang.serialization.RpcSerialization;
import com.zhang.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @ClassName RpcDecoder
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 16:02
 * @Version 1.0
 */
public class RpcDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        // 只有ByteBuf中的字节数大于消息头的18个字节时，才开始读数据
        if (in.readableBytes() < ProtocolConstants.HEADER_TOTAL_LEN) {
            return;
        }

        in.markReaderIndex();
        short magic = in.readShort();
        if (magic != ProtocolConstants.MAGIC) {
            throw new IllegalArgumentException("magic is illegal!" + magic);
        }

        byte version = in.readByte();

        byte serializeType = in.readByte();

        byte msgType = in.readByte();

        byte status = in.readByte();

        long requestId = in.readLong();

        int dataLength = in.readInt();

        // 如果消息没读完整，说明不够一个完整的数据包，需要重新开始读
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }

        byte[] data = new byte[dataLength];
        in.readBytes(data);

        MsgType type = MsgType.findByType(msgType);
        if (type == null) {
            return;
        }

        MsgHeader header = new MsgHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setSerialization(serializeType);
        header.setMsgType(msgType);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setMsgLen(dataLength);

        RpcSerialization rpcSerialization = SerializationFactory.get(serializeType);
        switch (type) {
            case REQUEST:
                RpcRequest request = rpcSerialization.deserialzie(data, RpcRequest.class);
                if (request != null) {
                    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
                    protocol.setMsgHeader(header);
                    protocol.setBody(request);
                    list.add(protocol);
                }
                break;
            case RESPONSE:
                RpcResponse response = rpcSerialization.deserialzie(data, RpcResponse.class);
                if (response != null) {
                    RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();
                    protocol.setMsgHeader(header);
                    protocol.setBody(response);
                    list.add(protocol);
                }
                break;
            case HEARTBEAT:
                // TODO
                break;
        }
    }
}
