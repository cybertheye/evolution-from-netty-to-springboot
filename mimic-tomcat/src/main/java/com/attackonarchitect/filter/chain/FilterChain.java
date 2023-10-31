package com.attackonarchitect.filter.chain;

import java.util.List;

/**
 * @description:
 */

public interface FilterChain extends Chain{
    void addFirst(List<Filter> filters);

    void addLast(List<Filter> filters);
}
