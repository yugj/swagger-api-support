package com.github.yugj.swagger.api.support.core.registry;

import com.alibaba.fastjson.JSON;
import com.github.yugj.swagger.api.support.core.debug.MultiEnvClient;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 反射代理类
 * @author yugj
 * @date 2019/9/17 上午10:45.
 */
public class Interface2BeanProxyHandler<T> implements InvocationHandler {

    private Class<T> interfaceType;
    private ApplicationContext applicationContext;
    private MultiEnvClient client;

    public Interface2BeanProxyHandler(Class<T> interfaceType,
                                      ApplicationContext applicationContext,
                                      MultiEnvClient client) {
        this.applicationContext = applicationContext;
        this.interfaceType = interfaceType;
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        Object bean = applicationContext.getBean(interfaceType);
        Method beanMethod = ReflectionUtils.findMethod(bean.getClass(), method.getName(), method.getParameterTypes());
        if (beanMethod == null || beanMethod.getReturnType() == null) {
            return null;
        }

        String bodyData = "{}";
        if (args != null && args.length > 0) {
            bodyData = JSON.toJSONString(args[0]);
        }

        String response = client.doPost(bodyData);
        return JSON.parseObject(response, beanMethod.getReturnType());
    }
}
