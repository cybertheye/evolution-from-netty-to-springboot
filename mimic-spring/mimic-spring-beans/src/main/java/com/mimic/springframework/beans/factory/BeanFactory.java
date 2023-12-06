
package com.mimic.springframework.beans.factory;


/**
 * bean factory
 */
public interface BeanFactory {

    /**
     * factory bean前缀
     */
    String FACTORY_BEAN_PREFIX = "&";


    /**
     * 根据bean名称返回bean
     *
     * @param name
     * @return
     * @throws BeansException
     */
    Object getBean(String name) throws BeansException;

    /**
     * 根据bean名称和类型返回bean
     *
     * @param name
     * @param requiredType
     * @param <T>
     * @return
     * @throws BeansException
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    /**
     * 根据bean名称和参数返回bean
     *
     * @param name
     * @param args
     * @return
     * @throws BeansException
     */
    Object getBean(String name, Object... args) throws BeansException;

    /**
     * 根据类型返回bean
     *
     * @param requiredType
     * @param <T>
     * @return
     * @throws BeansException
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;

    /**
     * 根据类型和参数返回bean
     *
     * @param requiredType
     * @param args
     * @param <T>
     * @return
     * @throws BeansException
     */
    <T> T getBean(Class<T> requiredType, Object... args) throws BeansException;

    /**
     * 根据bean名称判断是否包含该Bean
     *
     * @param name
     * @return
     */
    boolean containsBean(String name);

    /**
     * 根据名称判断是否是单例bean
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

    /**
     * 根据bean名称判断是否是原型bean
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

    /**
     * 根据bean名称获取bean类型
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    Class<?> getType(String name) throws NoSuchBeanDefinitionException;

    /**
     * 根据bean名称获取别名
     *
     * @param name
     * @return
     */
    String[] getAliases(String name);

}
