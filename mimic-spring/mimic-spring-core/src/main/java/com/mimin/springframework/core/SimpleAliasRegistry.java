
package com.mimin.springframework.core;


import com.mimin.springframework.util.Assert;
import com.mimin.springframework.util.ObjectUtils;
import com.mimin.springframework.util.StringUtils;
import com.mimin.springframework.util.StringValueResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单别名注册器
 */
public class SimpleAliasRegistry implements AliasRegistry {

    /**
     * 别名映射器
     */
    private final Map<String, String> aliasMap = new ConcurrentHashMap<>(16);


    @Override
    public void registerAlias(String name, String alias) {

        synchronized (this.aliasMap) {
            if (alias.equals(name)) {
                this.aliasMap.remove(alias);
            } else {
                String registeredName = this.aliasMap.get(alias);
                if (registeredName != null) {
                    if (registeredName.equals(name)) {
                        // An existing alias - no need to re-register
                        return;
                    }
                    if (!allowAliasOverriding()) {
                        throw new IllegalStateException("Cannot define alias '" + alias + "' for name '" +
                                name + "': It is already registered for name '" + registeredName + "'.");
                    }

                }
                checkForAliasCircle(name, alias);
                this.aliasMap.put(alias, name);

            }
        }
    }

    /**
     * 是否允许别名覆盖
     */
    protected boolean allowAliasOverriding() {
        return true;
    }

    /**
     * @param name
     * @param alias
     * @return
     */
    public boolean hasAlias(String name, String alias) {
        String registeredName = this.aliasMap.get(alias);
        return ObjectUtils.nullSafeEquals(registeredName, name) || (registeredName != null
                && hasAlias(name, registeredName));
    }

    @Override
    public void removeAlias(String alias) {
        synchronized (this.aliasMap) {
            String name = this.aliasMap.remove(alias);
            if (name == null) {
                throw new IllegalStateException("No alias '" + alias + "' registered");
            }
        }
    }

    @Override
    public boolean isAlias(String name) {
        return this.aliasMap.containsKey(name);
    }

    @Override
    public String[] getAliases(String name) {
        List<String> result = new ArrayList<>();
        synchronized (this.aliasMap) {
            retrieveAliases(name, result);
        }
        return StringUtils.toStringArray(result);
    }

    /**
     * 获取全部别名
     *
     * @param name
     * @param result
     */
    private void retrieveAliases(String name, List<String> result) {
        this.aliasMap.forEach((alias, registeredName) -> {
            if (registeredName.equals(name)) {
                result.add(alias);
                retrieveAliases(alias, result);
            }
        });
    }

    /**
     * 解析全部名称、别名
     *
     * @param valueResolver
     * @description 名称和对应别名未必就是最想要，还需要替换占位符或者进行一些大小写转换的操作，通过StringValueResolver操作，将原有的别名的与名称转换后再次建立映射关系，然后移除旧的映射关系。
     */
    public void resolveAliases(StringValueResolver valueResolver) {
        Assert.notNull(valueResolver, "StringValueResolver must not be null");
        synchronized (this.aliasMap) {
            Map<String, String> aliasCopy = new HashMap<>(this.aliasMap);
            aliasCopy.forEach((alias, registeredName) -> {
                String resolvedAlias = valueResolver.resolveStringValue(alias);
                String resolvedName = valueResolver.resolveStringValue(registeredName);
                if (resolvedAlias == null || resolvedName == null || resolvedAlias.equals(resolvedName)) {
                    this.aliasMap.remove(alias);
                } else if (!resolvedAlias.equals(alias)) {
                    String existingName = this.aliasMap.get(resolvedAlias);
                    if (existingName != null) {
                        if (existingName.equals(resolvedName)) {
                            // Pointing to existing alias - just remove placeholder
                            this.aliasMap.remove(alias);
                            return;
                        }
                        throw new IllegalStateException(
                                "Cannot register resolved alias '" + resolvedAlias + "' (original: '" + alias +
                                        "') for name '" + resolvedName + "': It is already registered for name '" +
                                        registeredName + "'.");
                    }
                    checkForAliasCircle(resolvedName, resolvedAlias);
                    this.aliasMap.remove(alias);
                    this.aliasMap.put(resolvedAlias, resolvedName);
                } else if (!registeredName.equals(resolvedName)) {
                    this.aliasMap.put(alias, resolvedName);
                }
            });
        }
    }

    /**
     * 检查是否存在环，比如： a-》b-》c-》a
     *
     * @param name
     * @param alias
     */
    protected void checkForAliasCircle(String name, String alias) {
        if (hasAlias(alias, name)) {
            throw new IllegalStateException("Cannot register alias '" + alias +
                    "' for name '" + name + "': Circular reference - '" +
                    name + "' is a direct or indirect alias for '" + alias + "' already");
        }
    }

    /**
     * 获取标准名，比如：a -> b -> c -> d， 那么通过 a、b、c获取的都是d
     *
     * @param name
     * @return
     */
    public String canonicalName(String name) {
        String canonicalName = name;
        // Handle aliasing...
        String resolvedName;
        do {
            resolvedName = this.aliasMap.get(canonicalName);
            if (resolvedName != null) {
                canonicalName = resolvedName;
            }
        }
        while (resolvedName != null);
        return canonicalName;
    }

}
