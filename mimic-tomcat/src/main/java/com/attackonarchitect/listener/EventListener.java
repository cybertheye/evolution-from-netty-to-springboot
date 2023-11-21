package com.attackonarchitect.listener;

/**
 * @description:
 */

public interface EventListener {

    /**
     * 基于事件的通知逻辑实现
     * @param event
     */
    void doNotify(Event event);

}
