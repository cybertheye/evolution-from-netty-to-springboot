

package com.mimic.springframework.context;


import com.mimic.springframework.beans.factory.BeanFactory;
import com.mimic.springframework.beans.factory.BeansException;
import com.mimic.springframework.beans.factory.config.NamedBeanHolder;


public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 注入模型： 不注入
     */
    int AUTOWIRE_NO = 0;

    /**
     * 注入模型： 按名称注入
     */
    int AUTOWIRE_BY_NAME = 1;

    /**
     * 注入模型： 按类型注入
     */
    int AUTOWIRE_BY_TYPE = 2;

    /**
     * 注入模型： 按构造函数注入
     */
    int AUTOWIRE_CONSTRUCTOR = 3;

    /**
     * 注入模型： 自动检测注入
     */
    @Deprecated
    int AUTOWIRE_AUTODETECT = 4;

    /**
     * Suffix for the "original instance" convention when initializing an existing
     * bean instance: to be appended to the fully-qualified bean class name,
     * e.g. "com.mypackage.MyClass.ORIGINAL", in order to enforce the given instance
     * to be returned, i.e. no proxies etc.
     *
     * @see #initializeBean(Object, String)
     * @see #applyBeanPostProcessorsBeforeInitialization(Object, String)
     * @see #applyBeanPostProcessorsAfterInitialization(Object, String)
     * @since 5.1
     */
    String ORIGINAL_INSTANCE_SUFFIX = ".ORIGINAL";


    //-------------------------------------------------------------------------
    // Typical methods for creating and populating external bean instances
    //-------------------------------------------------------------------------

    /**
     * 创建bean
     *
     * @param beanClass
     * @param <T>
     * @return
     * @throws BeansException
     */
    <T> T createBean(Class<T> beanClass) throws BeansException;

    /**
     * 给对应的bean注入属性
     *
     * @param existingBean
     * @throws BeansException
     */
    void autowireBean(Object existingBean) throws BeansException;

    /**
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object configureBean(Object existingBean, String beanName) throws BeansException;


    //-------------------------------------------------------------------------
    // Specialized methods for fine-grained control over the bean lifecycle
    //-------------------------------------------------------------------------


    Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;


    Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;


    void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
            throws BeansException;


    void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException;


    Object initializeBean(Object existingBean, String beanName) throws BeansException;


    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException;


    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException;


    void destroyBean(Object existingBean);


    //-------------------------------------------------------------------------
    // Delegate methods for resolving injection points
    //-------------------------------------------------------------------------


    <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType) throws BeansException;


//    Object resolveBeanByName(String name, DependencyDescriptor descriptor) throws BeansException;
//
//
//    @Nullable
//    Object resolveDependency(DependencyDescriptor descriptor, @Nullable String requestingBeanName) throws BeansException;
//
//
//    @Nullable
//    Object resolveDependency(DependencyDescriptor descriptor, @Nullable String requestingBeanName,
//                             @Nullable Set<String> autowiredBeanNames, @Nullable TypeConverter typeConverter) throws BeansException;

}
