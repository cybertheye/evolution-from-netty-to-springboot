package com.attackonarchitect.filter.chain;

import com.attackonarchitect.ComponentScanner;
import com.attackonarchitect.filter.container.FilterManager;
import com.attackonarchitect.filter.container.FilterManagerImplFactory;
import com.attackonarchitect.servlet.Servlet;

import java.util.List;

/**
 * @description:
 */
public class FilterChainImplFactory {
    private FilterChainImplFactory() {
    }


    public static FilterChain createFilterChain(Servlet servlet, String uri, ComponentScanner scanner) {
        //
        FilterManager filterManager = FilterManagerImplFactory.getFilterManager(scanner);
        List<Filter> filterList = filterManager.getSpecifedFilters(uri);

        return new DefaultFilterChain(servlet, filterList);
    }
}
