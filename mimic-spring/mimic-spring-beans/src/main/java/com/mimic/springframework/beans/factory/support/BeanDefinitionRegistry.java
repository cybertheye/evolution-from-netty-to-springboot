package com.mimic.springframework.beans.factory.support;

import com.mimic.springframework.beans.factory.BeanDefinitionStoreException;
import com.mimic.springframework.beans.factory.NoSuchBeanDefinitionException;
import com.mimic.springframework.beans.factory.config.BeanDefinition;
import com.mimin.springframework.core.AliasRegistry;


/**
 * BeanDefinition注册器
 */
public interface BeanDefinitionRegistry extends AliasRegistry {

    /**
     * 注册BeanDefinition
     *
     * @param beanName
     * @param beanDefinition
     * @throws BeanDefinitionStoreException
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws BeanDefinitionStoreException;

    /**
     * 移除BeanDefinition
     *
     * @param beanName
     * @throws NoSuchBeanDefinitionException
     */
    void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * 获取BeanDefinition
     *
     * @param beanName
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * 判断是否包含BeanDefinition
     *
     * @param beanName
     * @return
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 获取BeanDefinition名称列表
     *
     * @return
     */
    String[] getBeanDefinitionNames();

    /**
     * 获取BeanDefinition数量
     *
     * @return
     */
    int getBeanDefinitionCount();

    /**
     * 判断BeanName是否被占用
     *
     * @param beanName
     * @return
     */
    boolean isBeanNameInUse(String beanName);

}
