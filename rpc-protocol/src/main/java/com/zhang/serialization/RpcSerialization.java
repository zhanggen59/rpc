package com.zhang.serialization;

import java.io.IOException;

/**
 * @ClassName RpcSerialization
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 11:36
 * @Version 1.0
 */
public interface RpcSerialization {
    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialzie(byte[] data, Class<T> clazz) throws IOException;
}
