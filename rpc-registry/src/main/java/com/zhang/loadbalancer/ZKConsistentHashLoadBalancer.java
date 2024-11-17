package com.zhang.loadbalancer;

import com.zhang.model.ServiceMeta;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @ClassName ZKConsistentHashLoadBalancer
 * @Description
 * @Author zhanggen
 * @Date 2024/11/2 10:07
 * @Version 1.0
 */
public class ZKConsistentHashLoadBalancer implements ServiceLoadBalancer<ServiceInstance<ServiceMeta>> {
    private static final int VIRTUAL_NODE_SIZE = 10;

    private static final String VIRTUAL_NODE_SPLIT = "#";

    @Override
    public ServiceInstance<ServiceMeta> select(List<ServiceInstance<ServiceMeta>> servers, int hashCode) {
        // 1、构造哈希环
        TreeMap<Integer, ServiceInstance<ServiceMeta>> ring = makeHashRing(servers);

        // 2、分配节点
        return allocateNode(ring, hashCode);
    }

    /**
     * @Author ZG
     * @Description 根据哈希值分配节点
     * @Date 2024/11/2 10:25
     * @Param [ring, hashCode]
     * @return void
     **/
    public ServiceInstance<ServiceMeta> allocateNode(TreeMap<Integer, ServiceInstance<ServiceMeta>> ring, int hashCode) {
        // 找出大于或等于客户端hashCode的第一个节点
        Map.Entry<Integer, ServiceInstance<ServiceMeta>> entry = ring.ceilingEntry(hashCode);
        if (entry == null) {
            entry = ring.firstEntry(); // 如果没找到，直接取第一个值
        }

        return entry.getValue();
    }


    /**
     * @Author ZG
     * @Description 构造哈希环，计算出每个服务节点地址和端口对应的哈希值，TreeMap会对哈希值从小到大排序
     * @Date 2024/11/2 10:18
     * @Param [servers] 服务节点列表
     * @return java.util.TreeMap<java.lang.Integer,org.apache.curator.x.discovery.ServiceInstance<com.zhang.model.ServiceMeta>>
     **/
    public TreeMap<Integer, ServiceInstance<ServiceMeta>> makeHashRing(List<ServiceInstance<ServiceMeta>> servers) {
        TreeMap<Integer, ServiceInstance<ServiceMeta>> treeMap = new TreeMap<>();

        for (ServiceInstance<ServiceMeta> server : servers) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                treeMap.put((generateKey(server) + VIRTUAL_NODE_SPLIT + i).hashCode(), server);
            }
        }

        return treeMap;
    }

    /**
     * @Author ZG
     * @Description 使用地址+端口构造key
     * @Date 2024/11/2 10:18
     * @Param [instance]
     * @return java.lang.String
     **/
    public String generateKey(ServiceInstance<ServiceMeta> instance) {
        ServiceMeta payload = instance.getPayload();
        return String.join(":", payload.getAddr(), String.valueOf(payload.getPort()));
    }
}
