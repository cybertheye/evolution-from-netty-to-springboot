package com.attackonarchitect.listener.webcontext;

import com.attackonarchitect.listener.Event;

/**
 * @description:
 */
public class ServletContextEvent implements Event {
    private String name;
    private Object source;

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
