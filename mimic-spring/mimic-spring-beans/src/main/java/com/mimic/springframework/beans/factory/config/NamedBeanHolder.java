

package com.mimic.springframework.beans.factory.config;

import com.mimic.springframework.beans.factory.NamedBean;
import com.mimin.springframework.util.Assert;

/**
 * @param <T>
 */
public class NamedBeanHolder<T> implements NamedBean {

    private final String beanName;

    private final T beanInstance;


    public NamedBeanHolder(String beanName, T beanInstance) {
        Assert.notNull(beanName, "Bean name must not be null");
        this.beanName = beanName;
        this.beanInstance = beanInstance;
    }


    /**
     * Return the name of the bean.
     */
    @Override
    public String getBeanName() {
        return this.beanName;
    }

    /**
     * Return the corresponding bean instance.
     */
    public T getBeanInstance() {
        return this.beanInstance;
    }

}
