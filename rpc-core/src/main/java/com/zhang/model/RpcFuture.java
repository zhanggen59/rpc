package com.zhang.model;

import io.netty.util.concurrent.Promise;
import lombok.Data;

/**
 * @ClassName RpcFuture
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 18:07
 * @Version 1.0
 */
@Data
public class RpcFuture<T> {
    private Promise<T> promise;
    private long timeout;

    public RpcFuture(Promise<T> promise, long timeout) {
        this.promise = promise;
        this.timeout = timeout;
    }
}
