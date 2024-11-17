package com.zhang.api;

import com.zhang.model.ServiceMeta;

/**
 * @ClassName RegistryService
 * @Description 通用的注册中心接口
 * @Author zhanggen
 * @Date 2024/10/31 16:58
 * @Version 1.0
 */
public interface RegistryService {
    void registry(ServiceMeta serviceMeta) throws Exception;

    void unregistry(ServiceMeta serviceMeta) throws Exception;

    // 注册中心销毁
    void destroy() throws Exception;

    ServiceMeta discovery(String serviceName, int invokeHashCode) throws Exception;
}
