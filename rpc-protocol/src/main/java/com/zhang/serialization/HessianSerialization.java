package com.zhang.serialization;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ClassName HessianSerialization
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 11:42
 * @Version 1.0
 */
@Component
public class HessianSerialization implements RpcSerialization {
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        if (obj == null) {
            throw new NullPointerException("obj is null!");
        }

        byte[] results;
        HessianSerializerOutput output;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            output = new HessianSerializerOutput(os);
            output.writeObject(obj);
            output.flush();

            results = os.toByteArray();
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialzie(byte[] data, Class<T> clazz) throws IOException {
        if (data == null) {
            throw new NullPointerException("data is null");
        }

        T result;
        HessianSerializerInput input;
        try (ByteArrayInputStream is = new ByteArrayInputStream(data)) {
            input = new HessianSerializerInput(is);
            result = (T) input.readObject(clazz);
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return result;
    }
}
