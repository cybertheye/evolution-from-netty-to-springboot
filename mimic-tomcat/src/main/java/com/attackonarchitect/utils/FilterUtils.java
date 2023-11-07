package com.attackonarchitect.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @description:
 */
public class FilterUtils {
    private FilterUtils(){}


    /**
     * /hello/a/b
     *
     * /hello/a/b/*
     * /hello/a/b
     * /hello/a/*
     * /hello/b
     * /*
     *
     *
     * @param uri
     * @return
     */
    public static List<String> getMatchingFilterUri(String uri, Set<String> allFilterUri){

        List<String> ret = new ArrayList<>();
        String matcher = uri;
        /*
        uri 的路径递减进行 Filter 匹配
        /hello/a/b -> /hello/a/b/* ? , /hello/a/b ?
        /hello/a   -> /hello/a/* ?, /hello/a ?
        /hello     -> /hello/* ?, /hello ?
        /          -> /* ?
         */
        while(!"".equals(matcher)){
            if(allFilterUri.contains(matcher+"/*")){
                ret.add(matcher+"/*");
            }
            if(allFilterUri.contains(matcher)){
                ret.add(matcher);
            }
            matcher = matcher.substring(0,matcher.lastIndexOf("/"));
        }

        if(allFilterUri.contains("/*")){
            ret.add("/*");
        }
        return ret;
    }
}
