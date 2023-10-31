package com.attackonarchitect.filter.container;

import com.attackonarchitect.filter.chain.Filter;

import java.util.List;
import java.util.Set;

/**
 * @description:
 */
public interface FilterManager {
    //Filter getSpecifedFilter(String uri);
    List<Filter> getSpecifedFilters(String uri);
    Set<String> getAllFilterUri();
}
