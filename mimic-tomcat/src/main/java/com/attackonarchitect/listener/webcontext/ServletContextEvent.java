package com.attackonarchitect.listener.webcontext;

/**
 * @description:
 */
public class ServletContextEvent {
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
