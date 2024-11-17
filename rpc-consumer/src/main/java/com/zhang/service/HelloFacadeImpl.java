package com.zhang.service;

import com.zhang.api.HelloFacade;
import com.zhang.api.RpcService;

/**
 * @ClassName HelloFacadeImpl
 * @Description
 * @Author zhanggen
 * @Date 2024/11/4 15:31
 * @Version 1.0
 */
@RpcService(serviceType = HelloFacade.class, serviceVersion = "1.0.0")
public class HelloFacadeImpl implements HelloFacade {
    @Override
    public String hello(String name) {
        return "hello" + name;
    }
}
