package com.github.yugj.swagger.api.support.core.registry;

import com.github.yugj.swagger.api.support.core.debug.MultiEnvClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.ApplicationContext;


/**
 *
 * 接口生成实现工厂类
 *
 * @author yugj
 * @date 2019/9/17 上午10:40.
 */
public class Interface2BeanFactory<T> implements FactoryBean<T> {

    private Class<T> interfaceType;
    private ApplicationContext applicationContext;
    private MultiEnvClient client;


    public Interface2BeanFactory(Class<T> interfaceType,
                                 ApplicationContext applicationContext,
                                 MultiEnvClient client) {
        this.interfaceType = interfaceType;
        this.applicationContext = applicationContext;
        this.client = client;
    }

    @Override
    public T getObject() throws Exception {
        InvocationHandler handler = new Interface2BeanProxyHandler<>(interfaceType,applicationContext,client);

        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(),
                new Class[]{interfaceType}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceType;
    }
}
