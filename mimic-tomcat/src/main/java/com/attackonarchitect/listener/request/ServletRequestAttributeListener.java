package com.attackonarchitect.listener.request;

import com.attackonarchitect.listener.EventListener;

/**
 * @description:
 */

public interface ServletRequestAttributeListener extends EventListener {
    void requestAttributeAdded(ServletRequestAttributeEvent srae);
}
