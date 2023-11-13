package com.attackonarchitect.servlet;

import java.util.Map;

/**
 * @description:
 *
 *  抽象servlet的信息，包括类名，urlPattern，loadOnStartup，initParams
 *
 */
public class ServletInformation {
    private String clazzName;
    private String[] urlPattern;
    private int loadOnStartup;
    private Map<String, String> initParams;

    public ServletInformation(){
    }
    public ServletInformation(String clazzName, String[] urlPattern, int loadOnStartup, Map<String, String> initParams) {
        this.clazzName = clazzName;
        this.urlPattern = urlPattern;
        this.loadOnStartup = loadOnStartup;
        this.initParams = initParams;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public void setUrlPattern(String[] urlPattern) {
        this.urlPattern = urlPattern;
    }

    public void setLoadOnStartup(int loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }

    public void setInitParams(Map<String, String> initParams) {
        this.initParams = initParams;
    }

    public String getClazzName() {
        return clazzName;
    }

    public String[] getUrlPattern() {
        return urlPattern;
    }

    public int getLoadOnStartup() {
        return loadOnStartup;
    }

    public Map<String, String> getInitParams() {
        return initParams;
    }

}
