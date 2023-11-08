package com.attackonarchitect.handler;

import com.attackonarchitect.servlet.Servlet;
import com.attackonarchitect.servlet.ServletManager;

import java.util.Set;

/**
 * @description:
 */
public class RouteMaxMatchStrategy implements RouteStrategy {

    private ServletManager servletManager;

    public RouteMaxMatchStrategy(ServletManager servletManager) {
        this.servletManager = servletManager;
    }

    /**
     * /hello/a
     * 
     * 
     * /hello/a/b/*
     * /hello/a/b
     * /hello/a/*
     * /hello/a
     * /hello/*
     * /*
     * @param targetUri
     * @return
     */
    @Override
    public Servlet route(String targetUri) {
        
        String matchUri=null;
        Servlet target = null;

        Set<String> allURI = servletManager.getAllRequestUri();

        char[] chars = targetUri.toCharArray();
        //对比两次，先匹配 xxx/* ,然后再匹配 xxx，因为后者是精确匹配
        for(int index = 0; index < chars.length;){
            String matcher = null;
            if(chars[index] == '/'){
                matcher = targetUri.substring(0, index+1) + "*";
                if(allURI.contains(matcher)){
                    matchUri = matcher;
                }
            }
            
            index = tillNextSlash(index+1,chars);

            matcher = targetUri.substring(0, index);
            
            if(allURI.contains(matcher)){
                matchUri = matcher;
            }

        }

        if(matchUri==null){
            target = servletManager.getSpecifedServlet("default");
        }else{
            target = servletManager.getSpecifedServlet(matchUri);
        }
        return target;
    }

    private int tillNextSlash(int i, char[] chars) {
        for(;i<chars.length && Character.isAlphabetic(chars[i]) ; i++){}
        return i;
    }
}
