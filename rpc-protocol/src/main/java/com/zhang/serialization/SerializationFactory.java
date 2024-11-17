package com.zhang.serialization;

/**
 * @ClassName SerializationFactory
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 15:40
 * @Version 1.0
 */
public class SerializationFactory {

    public static RpcSerialization get(byte serializationType) {
        SerializationType type = SerializationType.findByType(serializationType);

        switch (type) {
            case JSON:
                return new JsonSerialization();
            case HESSIAN:
                return new HessianSerialization();
            default:
                throw new IllegalArgumentException("serialization type is illegal, " + serializationType);
        }
    }
}
