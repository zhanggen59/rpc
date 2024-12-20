package com.zhang.serialization;

/**
 * @ClassName SerializationException
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 14:23
 * @Version 1.0
 */
public class SerializationException extends RuntimeException {

    private static final long serialVersionUID = 3365624081242234230L;

    public SerializationException() {
        super();
    }

    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
