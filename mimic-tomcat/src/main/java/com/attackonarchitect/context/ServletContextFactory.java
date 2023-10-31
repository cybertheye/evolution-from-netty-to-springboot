package com.attackonarchitect.context;

import com.attackonarchitect.listener.Notifier;
import com.attackonarchitect.listener.webcontext.ServletContextEvent;
import com.attackonarchitect.listener.webcontext.ServletContextListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 */
public class ServletContextFactory {
    private ServletContextFactory() {
    }

    private static ServletContext cache;

    public static ServletContext getInstance(List<String> webListeners, Notifier notifier) {
        if (cache == null) {
            cache = ApplicationContext.getInstance();
            ((ApplicationContext) cache).setNotifiler(notifier);
            ServletContextEvent sce = new ServletContextEvent();
            sce.setSource(cache);
            sce.setName("ccccccccc");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    notifyServletContextListener(webListeners, sce);
                }
            }).start();
        }

        return cache;
    }

    private static List<ServletContextListener> servletContextListeners;

    private static void notifyServletContextListener(List<String> webListeners, ServletContextEvent event) {
        initServletContextListeners(webListeners);

        servletContextListeners.forEach(listener -> {
            listener.contextInitialized(event);
        });
    }

    private static void initServletContextListeners(List<String> webListeners) {
        servletContextListeners = new ArrayList<>();
        try {

            for (String webListener : webListeners) {
                Class<?> clazz = Class.forName(webListener);
                if (ServletContextListener.class.isAssignableFrom(clazz)) {
                    servletContextListeners.add((ServletContextListener) clazz.newInstance());
                }
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
