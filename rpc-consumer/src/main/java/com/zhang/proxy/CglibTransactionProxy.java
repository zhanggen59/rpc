package com.zhang.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @ClassName CglibTransactionProxy
 * @Description
 * @Author zhanggen
 * @Date 2024/11/3 16:32
 * @Version 1.0
 */
public class CglibTransactionProxy implements MethodInterceptor {
    private static Object target;

    public CglibTransactionProxy(Object target) {
        this.target = target;
    }

    public Object getProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("cglib start transaction");
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("cglib submit transaction");
        return result;
    }
}
