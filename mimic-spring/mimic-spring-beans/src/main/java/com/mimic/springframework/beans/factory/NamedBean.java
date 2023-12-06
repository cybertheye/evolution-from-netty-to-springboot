

package com.mimic.springframework.beans.factory;

/**
 * 命名的Bean
 */
public interface NamedBean {

    /**
     * 返回bean的名称
     *
     * @return
     */
    String getBeanName();

}
