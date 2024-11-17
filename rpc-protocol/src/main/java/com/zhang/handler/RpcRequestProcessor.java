package com.zhang.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RpcRequestProcessor
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 17:19
 * @Version 1.0
 */
public class RpcRequestProcessor {
    private static ThreadPoolExecutor executor;

    public static void submitRequest(Runnable task) {
        if (executor == null) {
            synchronized (RpcRequestProcessor.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));
                }
            }
        }

        executor.submit(task);
    }
}
