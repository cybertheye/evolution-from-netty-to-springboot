package com.attackonarchitect.webmvc.servlet;

import com.attackonarchitect.servlet.MimicServlet;
import com.attackonarchitect.webmvc.handler.MimicHandlerMethodManager;
import com.attackonarchitect.webmvc.handler.MimicHandlerMethodManagerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description:
 */
public abstract class FrameworkServlet extends MimicServlet {
    //Spring
    private static final Class<?> DEFAULT_SPRING_CONTEXT = ClassPathXmlApplicationContext.class;

    private ApplicationContext applicationContext;

    private MimicHandlerMethodManager manager;
    public void setContextConfigLocation(String configLocation){
        applicationContext = new AnnotationConfigApplicationContext(configLocation);
    }

    public MimicHandlerMethodManager getManager() {
        if(manager == null){
            manager = MimicHandlerMethodManagerFactory.getInstance(applicationContext);
        }
        return manager;
    }
}
