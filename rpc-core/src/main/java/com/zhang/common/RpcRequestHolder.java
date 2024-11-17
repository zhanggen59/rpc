package com.zhang.common;

import com.zhang.model.RpcFuture;
import com.zhang.model.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName RpcRequestHolder
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 18:09
 * @Version 1.0
 */
public class RpcRequestHolder {
    public static final AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    public static final Map<Long, RpcFuture<RpcResponse>> REQUEST_MAP = new ConcurrentHashMap<>();
}
