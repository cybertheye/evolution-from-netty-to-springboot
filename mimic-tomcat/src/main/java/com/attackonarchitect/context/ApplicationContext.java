package com.attackonarchitect.context;

import com.attackonarchitect.listener.*;
import com.attackonarchitect.listener.webcontext.ServletContextAttributeEvent;
import com.attackonarchitect.listener.webcontext.ServletContextAttributeListener;
import com.attackonarchitect.listener.webcontext.ServletContextEvent;
import com.attackonarchitect.listener.webcontext.ServletContextListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 */
public class ApplicationContext implements ServletRegisterContext {
    //////--------
    private ApplicationContext() {
    }

    private static ApplicationContext instance;

    public static ServletContext getInstance(Notifier notifier) {
        if(instance == null){
            instance = new ApplicationContext();
            instance.setNotifiler(notifier);
            ServletContextEvent sce = new ServletContextEvent();
            sce.setSource(instance);
            sce.setName("servletcontext");
            //触发通知，但是放在这里通知其实不合理
            //因为万一有其他的 ServletContext 实现
            //所以还是放在工厂里面更好感觉。
            notifier.notifyListeners(sce);

        }
        return instance;
    }

    private Map<String, Object> attributeDepot;

    @Override
    public <T> void setAttribute(String name, T obj) {
        this.getAttributeDepot().put(name, obj);
        ServletContextAttributeEvent event = new ServletContextAttributeEvent();
        event.setName(name);
        event.setValue(obj);

        getNotifiler().notifyListeners(event);
    }

    @Override
    public Object getAttribute(String name) {
        return this.getAttributeDepot().get(name);
    }


    private Notifier notifier;

    ///////getter setter

    private Map<String, Object> getAttributeDepot() {
        if (attributeDepot == null) {
            attributeDepot = new HashMap<>();
        }
        return attributeDepot;
    }

    public void setNotifiler(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public Notifier getNotifiler() {
        return this.notifier;
    }


}
