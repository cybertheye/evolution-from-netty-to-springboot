package com.attackonarchitect.webmvc.handler;

import java.lang.reflect.Method;

/**
 * @description:
 */
public class MimicHandlerMethod {
    private String url;
    private Object targetBean;
    private Method handlerMethod;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getTargetBean() {
        return targetBean;
    }

    public void setTargetBean(Object targetBean) {
        this.targetBean = targetBean;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(Method handlerMethod) {
        this.handlerMethod = handlerMethod;
    }
}
