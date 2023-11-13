package com.attackonarchitect.filter.chain;

import java.util.List;

/**
 * @description:
 * filter servlet重要组件,处理请求的一些公共逻辑
 */

public interface FilterChain extends Chain{
    void addFirst(List<Filter> filters);

    void addLast(List<Filter> filters);
}
