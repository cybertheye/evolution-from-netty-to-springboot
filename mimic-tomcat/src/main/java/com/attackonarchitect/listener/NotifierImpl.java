package com.attackonarchitect.listener;

import com.attackonarchitect.listener.request.ServletRequestAttributeEvent;
import com.attackonarchitect.listener.request.ServletRequestAttributeListener;
import com.attackonarchitect.listener.webcontext.ServletContextAttributeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 */
public class NotifierImpl implements Notifier{

    private List<ServletRequestAttributeListener> servletRequestAttributeListenerList;

    public List<ServletRequestAttributeListener> getServletRequestAttributeListenerList() {
        if(servletRequestAttributeListenerList == null){
            servletRequestAttributeListenerList = new ArrayList<>();
        }
        return servletRequestAttributeListenerList;
    }

    public NotifierImpl(List<String> webListeners) {
        init(webListeners);
    }

    private void init(List<String> webListeners) {

        webListeners.forEach(listenerClazzName->{
            try {
                Class<?> clazz = Class.forName(listenerClazzName);
                if(ServletRequestAttributeListener.class.isAssignableFrom(clazz)){
                    getServletRequestAttributeListenerList()
                            .add((ServletRequestAttributeListener) clazz.newInstance());
                }

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
    public void notifyListeners(Class<?> listener, Event event) {
        if(listener== ServletRequestAttributeListener.class){
            nofityRequestAttributeListeners(event);
        }
        if(listener == ServletContextAttributeListener.class){
            nofityContextAttributeListeners(event);
        }
    }

    private void nofityContextAttributeListeners(Event event) {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private void nofityRequestAttributeListeners(Event event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                servletRequestAttributeListenerList.forEach(listener->{
                    listener.requestAttributeAdded((ServletRequestAttributeEvent) event);
                });
            }
        }).start();
    }
}
