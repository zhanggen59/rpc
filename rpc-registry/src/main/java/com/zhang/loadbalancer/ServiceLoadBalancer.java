package com.zhang.loadbalancer;

import java.util.List;

/**
 * @ClassName ServiceLoadBalancer
 * @Description
 * @Author zhanggen
 * @Date 2024/11/2 10:07
 * @Version 1.0
 */
public interface ServiceLoadBalancer<T> {
    T select(List<T> servers, int hashCode);
}
