package com.mimic.springframework.beans.factory.config;

import com.mimin.springframework.lang.Nullable;

/**
 * 单例bean注册器
 */
public interface SingletonBeanRegistry {

    /**
     * 注册单例bean
     *
     * @param beanName
     * @param singletonObject
     */
    void registerSingleton(String beanName, Object singletonObject);

    /**
     * 获取单例bean
     *
     * @param beanName
     * @return
     */
    @Nullable
    Object getSingleton(String beanName);

    /**
     * 判断是否包含单例bean
     *
     * @param beanName
     * @return
     */
    boolean containsSingleton(String beanName);

    /**
     * 获取所有单例bean名称
     *
     * @return
     */
    String[] getSingletonNames();

    /**
     * 获取单例bean数量
     *
     * @return
     */
    int getSingletonCount();

    /**
     * 获取单例bean锁
     *
     * @return
     */
    Object getSingletonMutex();

}
