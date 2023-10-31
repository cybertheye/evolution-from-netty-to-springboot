package com.attackonarchitect.context;

import com.attackonarchitect.listener.*;
import com.attackonarchitect.listener.webcontext.ServletContextAttributeEvent;
import com.attackonarchitect.listener.webcontext.ServletContextAttributeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 */
public class ApplicationContext implements ServletRegisterContext {

    private Notifier notifier;

    public void setNotifiler(Notifier notifier) {
        this.notifier = notifier;
    }

    //////--------
    private ApplicationContext() {
    }


    private static class ApplicationContextHolder {
        static ApplicationContext instance = new ApplicationContext();
    }

    public static ServletContext getInstance() {
        return ApplicationContextHolder.instance;
    }

    private Map<String, Object> attributeDepot;

    private List<ServletContextAttributeListener> attributeListeners;


    @Override
    public void setAttribute(String name, Object obj) {
        this.getAttributeDepot().put(name, obj);
        ServletContextAttributeEvent event = new ServletContextAttributeEvent();
        event.setName(name);
        event.setValue(obj);
        this.notifyContextAttributeListeners(event);
    }

    private void notifyContextAttributeListeners(ServletContextAttributeEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAttributeListeners().forEach(listener -> {
                    listener.attributeAdded(event);
                });
            }
        }).start();
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public void register(EventListener listener) {
        if (listener instanceof ServletContextAttributeListener) {
            this.getAttributeListeners().add((ServletContextAttributeListener) listener);
        }

    }

    @Override
    public void registerAll(List<String> listeners) {
        try {
            for (String listenerClazzName : listeners) {
                Class<?> clazz = Class.forName(listenerClazzName);
                register((EventListener) clazz.newInstance());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Notifier getNotifiler() {
        return this.notifier;
    }


    ///////getter setter

    private Map<String, Object> getAttributeDepot() {
        if (attributeDepot == null) {
            attributeDepot = new HashMap<>();
        }
        return attributeDepot;
    }

    private List<ServletContextAttributeListener> getAttributeListeners() {
        if (attributeListeners == null) {
            attributeListeners = new ArrayList<>();
        }
        return attributeListeners;
    }


}
