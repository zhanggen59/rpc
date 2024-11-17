package com.zhang.api;

/**
 * @ClassName RegistryFactory
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 18:46
 * @Version 1.0
 */
public class RegistryFactory {
    private static volatile RegistryService registryService;

    public static RegistryService getInstance(String registryAddr, RegistryType type) throws Exception {
        if (registryService == null) {
            synchronized (RegistryFactory.class) {
                if (registryService == null) {
                    switch (type) {
                        case ZOOKEEPER:
                            registryService = new ZookeeperRegistryService(registryAddr);
                        case EUREKA:
                            registryService = new EurekaRegistryService();
                            break;
                    }
                }
            }
        }

        return registryService;
    }
}
