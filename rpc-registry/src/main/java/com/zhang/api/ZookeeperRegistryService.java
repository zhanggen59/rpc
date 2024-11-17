package com.zhang.api;

import com.zhang.common.RpcServiceHelper;
import com.zhang.loadbalancer.ZKConsistentHashLoadBalancer;
import com.zhang.model.ServiceMeta;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.List;

/**
 * @ClassName ZookeeperRegistryService
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 18:49
 * @Version 1.0
 */
public class ZookeeperRegistryService implements RegistryService{
    private static final int MAX_RETRIES = 3;

    private static final int BASE_SLEEP_TIME_MS = 1000;

    private static final String ZK_BASE_PATH = "/rpc";

    private final ServiceDiscovery<ServiceMeta> serviceDiscovery;

    public ZookeeperRegistryService(String serviceAddr) throws Exception{
        // 重试策略
        RetryPolicy retry = new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES);
        // 创建CuratorFramework实例
        CuratorFramework client = CuratorFrameworkFactory.newClient(serviceAddr, retry);
        client.start();

        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH)
                .build();

        this.serviceDiscovery.start();
    }

    @Override
    public void registry(ServiceMeta serviceMeta) throws Exception {
        // 一般来说，会将相同版本的RPC服务归类在一起，所以将serviceInstance的名称name根据服务名称和服务版本赋值
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance.<ServiceMeta>builder()
                .name(RpcServiceHelper.buildServiceKey(serviceMeta.getName(), serviceMeta.getVersion()))
                .address(serviceMeta.getAddr())
                .port(serviceMeta.getPort())
                .payload(serviceMeta) // 用户自定义可选属性
                .build();

        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unregistry(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance.<ServiceMeta>builder()
                .name(RpcServiceHelper.buildServiceKey(serviceMeta.getName(), serviceMeta.getVersion()))
                .address(serviceMeta.getAddr())
                .port(serviceMeta.getPort())
                .payload(serviceMeta)
                .build();

        serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public void destroy() throws Exception {
        serviceDiscovery.close();
    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokeHashCode) throws Exception {
        // 1、首先找出被调用服务所有节点列表
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);

        // 2、根据一致性哈希算法找出相应的服务节点
        ServiceInstance<ServiceMeta> instance = new ZKConsistentHashLoadBalancer().select((List<ServiceInstance<ServiceMeta>>) serviceInstances, invokeHashCode);

        if (instance != null) {
            return instance.getPayload();
        }

        return null;
    }
}
