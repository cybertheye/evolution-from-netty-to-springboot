package com.attackonarchitect.listener.webcontext;

import com.attackonarchitect.listener.EventListener;

/**
 * @description:
 */
public interface ServletContextListener extends EventListener {
    void contextInitialized(ServletContextEvent sce);
}
