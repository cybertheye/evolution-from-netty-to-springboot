package com.attackonarchitect.filter.container;

import com.attackonarchitect.ComponentScanner;

/**
 * @description:
 */
public class FilterManagerImplFactory {
    private FilterManagerImplFactory(){}

    public static FilterManager getFilterManager(ComponentScanner scanner){
        return FilterManagerImpl.getInstance(scanner);
    }
}
