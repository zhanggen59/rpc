package com.zhang.api;

import com.zhang.model.ServiceMeta;

/**
 * @ClassName EurekaRegistryService
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 18:49
 * @Version 1.0
 */
public class EurekaRegistryService implements RegistryService{
    @Override
    public void registry(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public void unregistry(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokeHashCode) throws Exception {
        return null;
    }
}
