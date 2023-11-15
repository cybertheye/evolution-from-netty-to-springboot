package com.attackonarchitect.filter.container;

import com.attackonarchitect.ComponentScanner;
import com.attackonarchitect.filter.chain.Filter;
import com.attackonarchitect.utils.FilterUtils;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        // matchingFilterUri按匹配的优先级, 从完全匹配到部分匹配 有序排列
        List<String> matchingFilterUri = FilterUtils.getMatchingFilterUri(uri,this.getAllFilterUri());

        Map<String, Set<String>> webFilterComponents = componentScanner.getWebFilterComponents();

        // 按照层级关系, 完全匹配, 到部分匹配, 先对同一层级内的filter进行排序,再对总的进行排序
        Optional<ArrayList<String>> clazzNameSet = matchingFilterUri.stream()
                .map(new Function<String, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> apply(String s) {
                        // 对同一层级内的filter进行排序
                        return webFilterComponents.get(s).stream().sorted(new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return componentScanner.getWebFilterComponentsOrder().get(o1) - componentScanner.getWebFilterComponentsOrder().get(o2);
                            }
                        }).collect(Collectors.toCollection(ArrayList::new));
                    }
                })
                .reduce(new BinaryOperator<ArrayList<String>>() {
                    @Override
                    public ArrayList<String> apply(ArrayList<String> s1, ArrayList<String> s2) {
                        s1.addAll(s2);
                        return s1;
                    }
                });

        // 需要对重复的filter去重,前面层级出现过的filter,后面层级不再出现
        clazzNameSet.orElse(new ArrayList<>()).stream().distinct().forEach(clzName->{
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
