package com.zhang.api;

import com.zhang.model.RpcConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName RpcConsumerPostProcessor
 * @Description
 * @Author zhanggen
 * @Date 2024/11/1 10:07
 * @Version 1.0
 */
@Component
public class RpcConsumerPostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerPostProcessor.class);

    private ApplicationContext context;

    private ClassLoader classLoader;

    private final Map<String, BeanDefinition> rpcBeanDefinitions = new LinkedHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 从beanFactory中获取所有的Bean定义信息
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null) {
                Class<?> aClass = ClassUtils.resolveClassName(beanClassName, this.classLoader);
                // 分别对每个Bean的所有field检测，当有RpcReference注解，就构造RpcReferenceBean定义，并对其成员变量赋值
                ReflectionUtils.doWithFields(aClass, this::parseRpcReference);
            }
        }

        // 构造完RpcReferenceBean定义之后，会将其重新注册到Spring容器中
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        this.rpcBeanDefinitions.forEach((beanName, beanDefinition) -> {
            if (context.containsBean(beanName)) {
                throw new IllegalArgumentException("spring context already has a bean named " + beanName);
            }

            registry.registerBeanDefinition(beanName, rpcBeanDefinitions.get(beanName));
            LOGGER.info("registry RpcReferenceBean {} success", beanName);
        });
    }

    public void parseRpcReference(Field field) {
        RpcReference rpcReference = AnnotationUtils.getAnnotation(field, RpcReference.class);
        if (rpcReference != null) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcReferenceBean.class);
            builder.setInitMethodName(RpcConstant.INIT_METHOD_NAME);
            builder.addPropertyValue("serviceType", field.getType());
            builder.addPropertyValue("serviceVersion", rpcReference.serviceVersion());
            builder.addPropertyValue("registryAddr", rpcReference.registryAddr());
            builder.addPropertyValue("registryType", rpcReference.registryType());
            builder.addPropertyValue("timeout", rpcReference.timeout());

            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            rpcBeanDefinitions.put(field.getName(), beanDefinition);
        }
    }
}
