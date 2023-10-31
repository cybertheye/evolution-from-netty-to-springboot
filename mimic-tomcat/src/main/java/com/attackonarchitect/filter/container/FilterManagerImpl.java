package com.attackonarchitect.filter.container;

import com.attackonarchitect.ComponentScanner;
import com.attackonarchitect.filter.chain.Filter;
import com.attackonarchitect.utils.FilterUtils;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * @description:
 */
public class FilterManagerImpl implements FilterManager{
    private ComponentScanner componentScanner;
    private FilterManagerImpl(){}

    private static class FilterManangerImplHolder{
        static FilterManagerImpl instance = new FilterManagerImpl();
    }

    static FilterManager getInstance(ComponentScanner componentScanner){
        FilterManagerImpl impl = FilterManangerImplHolder.instance;
        impl.setComponentScanner(componentScanner);
        return impl;
    }



    private Map<String,Filter> filterDepot = new HashMap<>();

    private Filter getSpecifedFilter(String clazzName) {
        Filter ret = null;
        try {
            if (!filterDepot.containsKey(clazzName)) {
                Class<?> clazz = Class.forName(clazzName);
                Filter instance = (Filter)clazz.newInstance();
                filterDepot.put(clazzName,instance);
            }
            ret = filterDepot.get(clazzName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    @Override
    public List<Filter> getSpecifedFilters(String uri) {

        List<Filter> ret = new ArrayList<>();
        List<String> matchingFilterUri = FilterUtils.getMatchingFilterUri(uri,this.getAllFilterUri());

        Map<String, Set<String>> webFilterComponents = componentScanner.getWebFilterComponents();

        Optional<Set<String>> clazzNameSet = matchingFilterUri.stream()
                .map(new Function<String, Set<String>>() {
                    @Override
                    public Set<String> apply(String s) {
                        return webFilterComponents.get(s);
                    }
                })
                .reduce(new BinaryOperator<Set<String>>() {
                    @Override
                    public Set<String> apply(Set<String> s1, Set<String> s2) {
                        s1.addAll(s2);
                        return s1;
                    }
                });


        clazzNameSet.orElse(new HashSet<>()).forEach(clzName->{
            ret.add(this.getSpecifedFilter(clzName));
        });

        return ret;
    }

    @Override
    public Set<String> getAllFilterUri() {
        return componentScanner.getWebFilterComponents().keySet();
    }


    public ComponentScanner getComponentScanner() {
        return componentScanner;
    }

    public void setComponentScanner(ComponentScanner componentScanner) {
        this.componentScanner = componentScanner;
    }
}
