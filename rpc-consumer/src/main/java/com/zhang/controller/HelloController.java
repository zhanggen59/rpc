package com.zhang.controller;

import com.zhang.api.HelloFacade;
import com.zhang.api.RpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HelloController
 * @Description
 * @Author zhanggen
 * @Date 2024/11/4 15:27
 * @Version 1.0
 */
@RestController
public class HelloController {
    @SuppressWarnings({"SpringJavaAutowiredFieldsWarningInspection", "SpringJavaInjectionPointsAutowiringInspection"})
    @RpcReference(serviceVersion = "1.0.0", timeout = 3000)
    private HelloFacade helloFacade;

    @GetMapping(value = "/hello")
    public String sayHello() {
        return helloFacade.hello("mini rpc");
    }
}
