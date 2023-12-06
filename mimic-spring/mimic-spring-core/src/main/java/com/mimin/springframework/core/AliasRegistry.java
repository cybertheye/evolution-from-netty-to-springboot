
package com.mimin.springframework.core;

/**
 * 别名注册器
 */
public interface AliasRegistry {

    /**
     * 注册别名
     *
     * @param name
     * @param alias
     */
    void registerAlias(String name, String alias);

    /**
     * 移除别名
     *
     * @param alias
     */
    void removeAlias(String alias);

    /**
     * 是否是别名
     *
     * @param name
     * @return
     */
    boolean isAlias(String name);

    /**
     * 获取别名列表
     *
     * @param name
     * @return
     */
    String[] getAliases(String name);

}
