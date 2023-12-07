package com.attackonarchitect.listener;

import com.attackonarchitect.listener.request.ServletRequestAttributeEvent;
import com.attackonarchitect.listener.request.ServletRequestAttributeListener;
import com.attackonarchitect.listener.webcontext.ServletContextAttributeEvent;
import com.attackonarchitect.listener.webcontext.ServletContextAttributeListener;
import com.attackonarchitect.listener.webcontext.ServletContextEvent;
import com.attackonarchitect.listener.webcontext.ServletContextListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 */
public class NotifierImpl implements Notifier{

    //web context
    private List<ServletContextListener> servletContextListenersList;
    private List<ServletContextAttributeListener> servletContextAttributeListenerList;

    //request
    private List<ServletRequestAttributeListener> servletRequestAttributeListenerList;

    //todo 添加其他的 listener

    private List<EventListener> listeners=new ArrayList<>(16);


    public NotifierImpl(List<String> webListeners) {
//        init(webListeners);
        addListenersByPath(webListeners);
    }


    private void init(List<String> webListeners) {

        webListeners.forEach(listenerClazzName->{
            try {
                Class<?> clazz = Class.forName(listenerClazzName);
                //web context
                listeners.add((EventListener) clazz.newInstance());
//                if (ServletContextListener.class.isAssignableFrom(clazz)) {
//                    getServletContextListenersList()
//                            .add((ServletContextListener) clazz.newInstance());
//                }
//                if(ServletContextAttributeListener.class.isAssignableFrom(clazz)){
//                    getServletContextAttributeListenerList()
//                            .add((ServletContextAttributeListener) clazz.newInstance());
//                }
//
//                // request
//                if(ServletRequestAttributeListener.class.isAssignableFrom(clazz)){
//                    getServletRequestAttributeListenerList()
//                            .add((ServletRequestAttributeListener) clazz.newInstance());
//                }

                // session
                // todo 增加其他类型的 Listener



            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }


        });
    }

    @Override
    public void addListeners(List<EventListener> eventListeners) {
        //doSomething
    }

    @Override
    public void addListenersByPath(List<String> webListeners) {
        webListeners.forEach(listenerClazzName->{
            try {
                Class<?> clazz = Class.forName(listenerClazzName);
                //web context
                listeners.add((EventListener) clazz.newInstance());
                // session
                // todo 增加其他类型的 Listener
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }


        });
    }

    @Override
    public List<EventListener> getListeners() {
        return listeners;
    }

    @Override
    public void notifyListeners(Event event) {
//        if(listener== ServletRequestAttributeListener.class){
//            notifyServletRequestAttributeListeners(event);
//        }
//        if(listener == ServletContextAttributeListener.class){
//            notifyServletContextAttributeListeners(event);
//        }
//        if(listener == ServletContextListener.class){
//            notifyServletContextListeners(event);
//        }
        new Thread(()-> listeners.forEach(d -> d.doNotify(event))).start();

        //添加其他类型

    }

    private void notifyServletContextListeners(Event event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getServletContextListenersList()
                        .forEach(listener -> {
                            listener.contextInitialized((ServletContextEvent) event);
                        });
            }
        }).start();

    }

    private void notifyServletContextAttributeListeners(Event event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getServletContextAttributeListenerList().forEach(listener->{
                    listener.attributeAdded((ServletContextAttributeEvent) event);
                });
            }
        }).start();
    }

    private void notifyServletRequestAttributeListeners(Event event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getServletRequestAttributeListenerList().forEach(listener->{
                    listener.requestAttributeAdded((ServletRequestAttributeEvent) event);
                });
            }
        }).start();
    }




    ////getter,setter


    public List<ServletContextAttributeListener> getServletContextAttributeListenerList() {
        if(servletContextAttributeListenerList == null){
            servletContextAttributeListenerList = new ArrayList<>();
        }
        return servletContextAttributeListenerList;
    }

    public List<ServletContextListener> getServletContextListenersList() {
        if(servletContextListenersList == null){
            servletContextListenersList = new ArrayList<>();
        }
        return servletContextListenersList;
    }

    public List<ServletRequestAttributeListener> getServletRequestAttributeListenerList() {
        if(servletRequestAttributeListenerList == null){
            servletRequestAttributeListenerList = new ArrayList<>();
        }
        return servletRequestAttributeListenerList;
    }

}
