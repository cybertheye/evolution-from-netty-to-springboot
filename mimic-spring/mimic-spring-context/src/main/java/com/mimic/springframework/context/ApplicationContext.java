

package com.mimic.springframework.context;


import com.mimin.springframework.lang.Nullable;

public interface ApplicationContext {

    /**
     * 获取应用上下文的ID
     * @return
     */
    @Nullable
    String getId();

    /**
     * 获取应用名称
     * @return
     */
    String getApplicationName();

    /**
     * 获取应用显示名称
     * @return
     */
    String getDisplayName();

    /**
     * 获取应用启动时间
     * @return
     */
    long getStartupDate();

    /**
     * 获取父上下文
     * @return
     */
    @Nullable
    ApplicationContext getParent();


    AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;

}
