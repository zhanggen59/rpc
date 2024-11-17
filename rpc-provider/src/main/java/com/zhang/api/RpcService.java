package com.zhang.api;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName RpcService
 * @Description 以注解的方式暴露服务，服务消费者必须指定完全相同的属性才能调用
 * @Author zhanggen
 * @Date 2024/10/31 16:19
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {
    // 服务类型
    Class<?> serviceType() default Object.class;

    // 服务版本
    String serviceVersion() default "1.0";
}
