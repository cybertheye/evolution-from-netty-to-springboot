package com.attackonarchitect.listener.webcontext;

import com.attackonarchitect.listener.EventListener;

/**
 * @description:
 */

public interface ServletContextAttributeListener extends EventListener {
    void attributeAdded(ServletContextAttributeEvent scae);
}
