package com.zhang.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * @ClassName JsonSerialization
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 14:08
 * @Version 1.0
 */
public class JsonSerialization implements RpcSerialization{
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = generate(JsonInclude.Include.ALWAYS); // 序列化所有属性
    }

    public static ObjectMapper generate(JsonInclude.Include include) {
        ObjectMapper customMapper = new ObjectMapper();

        customMapper.setSerializationInclusion(include); // 用于指定序列化时包含的属性类型
        customMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 反序列化时遇到未知属性跳过，不报错
        customMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true); // 反序列化枚举类型时如果是数字，则报错
        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return customMapper;
    }

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return obj instanceof String ? ((String) obj).getBytes() : MAPPER.writeValueAsBytes(obj).toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialzie(byte[] data, Class<T> clazz) throws IOException {
        return MAPPER.readValue(Arrays.toString(data), clazz);
    }
}
