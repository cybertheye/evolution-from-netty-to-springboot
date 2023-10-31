package com.attackonarchitect.listener.request;

import com.attackonarchitect.listener.EventListener;

/**
 * @description:
 */

public interface ServletRequestListener extends EventListener {
    void requestInitialized();
}
