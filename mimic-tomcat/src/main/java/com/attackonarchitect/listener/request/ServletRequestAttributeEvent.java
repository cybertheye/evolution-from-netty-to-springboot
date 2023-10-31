package com.attackonarchitect.listener.request;

import com.attackonarchitect.listener.Event;
import com.attackonarchitect.listener.EventListener;

/**
 * @description:
 */
public class ServletRequestAttributeEvent implements Event {
    private String name;
    private Object value;

    public ServletRequestAttributeEvent(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
