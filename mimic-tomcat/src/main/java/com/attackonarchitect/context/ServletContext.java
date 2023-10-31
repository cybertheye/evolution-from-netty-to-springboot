package com.attackonarchitect.context;

import com.attackonarchitect.listener.EventListener;
import com.attackonarchitect.listener.Notifier;

import java.util.List;

/**
 * @description:
 */

public interface ServletContext {

    void setAttribute(String name, Object obj);
    Object getAttribute(String name);
    void register(EventListener listener);

    void registerAll(List<String> listeners);


    Notifier getNotifiler();
}
