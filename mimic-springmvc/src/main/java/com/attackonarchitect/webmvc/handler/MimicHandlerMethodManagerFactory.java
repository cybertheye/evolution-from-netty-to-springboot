package com.attackonarchitect.webmvc.handler;

import org.springframework.context.ApplicationContext;

/**
 * @description:
 */
public class MimicHandlerMethodManagerFactory {
    private MimicHandlerMethodManagerFactory(){}

    private static MimicHandlerMethodManager manager;
    public static MimicHandlerMethodManager getInstance(ApplicationContext context){
        if(manager == null){
            manager = new MimicHandlerMethodManagerImpl(context);
        }
        return manager;
    }
}
