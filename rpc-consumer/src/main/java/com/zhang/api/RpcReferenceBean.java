package com.zhang.api;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @ClassName RpcReferenceBean
 * @Description 使用RpcReference注解的属性，通过一个自定义bean完成所有方法的拦截
 * @Author zhanggen
 * @Date 2024/11/1 9:49
 * @Version 1.0
 */
public class RpcReferenceBean implements FactoryBean<Object> {
    private Class<?> serviceType;

    private String serviceVersion;

    private String registryAddr;

    private String registryType;

    private long timeout;

    private Object object;


    @Override
    public Object getObject() throws Exception {
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return serviceType;
    }

    public void init() throws Exception {
        // TODO 生成动态代理对象，并赋值给object
        RegistryService registryService = RegistryFactory.getInstance(registryAddr, RegistryType.valueOf(this.registryType));

        this.object = Proxy.newProxyInstance(serviceType.getClassLoader(),
                new Class<?>[]{serviceType},
                new RpcProxy(serviceVersion, timeout, registryService));
    }

    public void setServiceType(Class<?> serviceType) {
        this.serviceType = serviceType;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public void setRegistryAddr(String registryAddr) {
        this.registryAddr = registryAddr;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
