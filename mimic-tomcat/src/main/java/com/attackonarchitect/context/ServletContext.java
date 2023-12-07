package com.attackonarchitect.context;

import com.attackonarchitect.http.session.SessionManager;
import com.attackonarchitect.listener.Notifier;


/**
 * @description:
 */

public interface ServletContext {

    /**
     * 设置一个属性
     * @param name
     * @param obj
     */
    <T> void setAttribute(String name, T obj);

    /**
     * 取一个属性
     * @param name
     * @return
     */
     Object getAttribute(String name);
//    void register(EventListener listener);
//
//    void registerAll(List<String> listeners);
//

    Notifier getNotifiler();

    SessionManager getSessionManager();
}
