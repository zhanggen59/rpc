package com.zhang.api;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName RpcReference
 * @Description 对使用者来说，只是通过RpcReference注解来订阅服务，并不关注底层调用细节
 * @Author zhanggen
 * @Date 2024/10/31 17:19
 * @Version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcReference {
    String serviceVersion() default "1.0";

    String registryAddr() default "127.0.0.1:2181";

    String registryType() default "ZOOKEEPER";

    long timeout() default 5000;
}
