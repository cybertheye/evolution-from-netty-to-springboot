/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mimic.springframework.beans.factory.config;

import com.sun.istack.internal.Nullable;

/**
 * bean 定义
 */
public interface BeanDefinition {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    // Modifiable attributes

    /**
     * 父级bean definition 名称
     */
    void setParentName(@Nullable String parentName);

    /**
     * 获取父级bean definition 名称
     */
    @Nullable
    String getParentName();

    /**
     * bean对应的class字符串
     *
     * @param beanClassName
     */
    void setBeanClassName(@Nullable String beanClassName);

    /**
     * 获取bean对应的字符串
     *
     * @return
     */
    String getBeanClassName();

    /**
     * 设置作用域
     *
     * @see #SCOPE_SINGLETON
     * @see #SCOPE_PROTOTYPE
     */
    void setScope(@Nullable String scope);

    /**
     * 获取作用域
     */
    @Nullable
    String getScope();

    /**
     * 懒加载
     */
    void setLazyInit(boolean lazyInit);

    /**
     * 是否懒加载
     */
    boolean isLazyInit();

    /**
     * 设置依赖bean
     */
    void setDependsOn(@Nullable String... dependsOn);

    /**
     * Return the bean names that this bean depends on.
     */
    @Nullable
    String[] getDependsOn();

    /**
     * 自动注入模型
     */
    void setAutowireCandidate(boolean autowireCandidate);

    boolean isAutowireCandidate();

    /**
     * 设置primary
     */
    void setPrimary(boolean primary);

    /**
     * 是否primary
     */
    boolean isPrimary();

    /**
     * Specify the factory bean to use, if any.
     * This the name of the bean to call the specified factory method on.
     *
     * @see #setFactoryMethodName
     */
    void setFactoryBeanName(@Nullable String factoryBeanName);

    /**
     * Return the factory bean name, if any.
     */
    @Nullable
    String getFactoryBeanName();

    /**
     * 设置工厂方法
     */
    void setFactoryMethodName(@Nullable String factoryMethodName);

    /**
     * 获取工厂方法
     */
    @Nullable
    String getFactoryMethodName();

    /**
     * Set the name of the initializer method.
     *
     * @since 5.1
     */
    void setInitMethodName(@Nullable String initMethodName);

    /**
     * Return the name of the initializer method.
     *
     * @since 5.1
     */
    @Nullable
    String getInitMethodName();

    /**
     * Set the name of the destroy method.
     *
     * @since 5.1
     */
    void setDestroyMethodName(@Nullable String destroyMethodName);

    /**
     * Return the name of the destroy method.
     *
     * @since 5.1
     */
    @Nullable
    String getDestroyMethodName();

    /**
     * bean定义类别
     *
     * @param role
     */
    void setRole(int role);

    /**
     * bean定义类别
     *
     * @return
     */
    int getRole();

    /**
     * 描述
     *
     * @param description
     */
    void setDescription(@Nullable String description);

    String getDescription();


    // Read-only attributes

    /**
     * 是否单例
     */
    boolean isSingleton();

    /**
     * 是否原型
     */
    boolean isPrototype();

    /**
     * 是否抽象
     */
    boolean isAbstract();

    /**
     * 获取原始bean定义
     */
    @Nullable
    BeanDefinition getOriginatingBeanDefinition();

}
