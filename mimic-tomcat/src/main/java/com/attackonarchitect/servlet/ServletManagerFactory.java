package com.attackonarchitect.servlet;

import com.attackonarchitect.ComponentScanner;
import com.attackonarchitect.context.ServletContext;

/**
 * @description:
 */
public class ServletManagerFactory {
    private ServletManagerFactory(){}

    private static ServletManager servletManager;
    public static ServletManager getInstance(ComponentScanner provider, ServletContext servletContext){
        if(servletManager ==null){
            servletManager=ServletManagerImpl.getInstance(provider,servletContext);
        }
        return servletManager;
    }
}
