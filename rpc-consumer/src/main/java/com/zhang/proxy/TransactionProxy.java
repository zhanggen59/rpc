package com.zhang.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName TransactionProxy
 * @Description JDK动态代理
 * @Author zhanggen
 * @Date 2024/11/2 11:03
 * @Version 1.0
 */
public class TransactionProxy {

    private Object target; // 被代理的对象

    public TransactionProxy(Object target) {
        this.target = target;
    }

    public Object getProxyInstance() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    System.out.println("start transaction");
                    Object result = method.invoke(target, args);
                    System.out.println("submit transaction");
                    return result;
                });
    }
}
