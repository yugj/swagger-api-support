package com.github.yugj.swagger.api.support.core.registry;

import com.github.yugj.swagger.api.support.core.debug.MultiEnvClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author yugj
 * @date 2019/9/17 上午9:35.
 */
public class Interface2BeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, ApplicationContextAware {

    private static final String DEFAULT_CLAZZ_PATTERN = "**/*.class";

    private MetadataReaderFactory metadataReaderFactory;
    private ResourcePatternResolver resourcePatternResolver;
    private ApplicationContext applicationContext;
    private MultiEnvClient client;

    private String scanPackage;

    public Interface2BeanDefinitionRegistry(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        //反射获取接口的clazz列表
        Set<Class<?>> beanClazzSet = getInterfaceClazz(scanPackage);

        for (Class beanClazz : beanClazzSet) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();

            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClazz);
            definition.getConstructorArgumentValues().addGenericArgumentValue(applicationContext);
            definition.getConstructorArgumentValues().addGenericArgumentValue(client);

            definition.setBeanClass(Interface2BeanFactory.class);

            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            registry.registerBeanDefinition(beanClazz.getSimpleName(), definition);
        }

    }

    /**
     * 查询指定包下的接口 controller 类
     * @param basePackage basePackage
     * @return Set<Class<?>> Set<Class<?>>
     */
    private Set<Class<?>> getInterfaceClazz(String basePackage) {

        Set<Class<?>> set = new LinkedHashSet<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(basePackage) + '/' + DEFAULT_CLAZZ_PATTERN;
        try {
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (!resource.isReadable()) {
                    continue;
                }
                MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                String className = metadataReader.getClassMetadata().getClassName();
                Class<?> clazz;
                try {
                    clazz = Class.forName(className);
                    // swagger api 约定只暴露接口
                    if (clazz.isInterface()) {
                        set.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

    private String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(this.getEnvironment().resolveRequiredPlaceholders(basePackage));
    }

    private Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.applicationContext = context;
        this.client = applicationContext.getBean(MultiEnvClient.class);


    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }
}
