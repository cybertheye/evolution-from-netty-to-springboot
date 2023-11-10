package com.attackonarchitect.servlet;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 */
public class ServletInformationBuilder {
    private String clazzName;
    private String[] urlPattern;
    private int loadOnStartup;
    private Map<String, String> initParams;


    public ServletInformationBuilder setClassName(String clazzName) {
        this.clazzName = clazzName;
        return this;
    }

    public ServletInformationBuilder setUrlPattern(String[] urlPattern) {
        this.urlPattern = urlPattern;
        return this;
    }

    public ServletInformationBuilder setLoadOnStartup(int loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
        return this;
    }

    public Map<String, String> getInitParams() {
        if (initParams == null) {
            initParams = new HashMap<>();
        }
        return initParams;
    }

    public ServletInformationBuilder setInitParams(WebInitParam[] webInitParams) {
        for (WebInitParam webInitParam : webInitParams) {
            getInitParams().put(webInitParam.name(), webInitParam.value());
        }
        return this;
    }

    public ServletInformation build() {
        return new ServletInformation(clazzName, urlPattern, loadOnStartup, initParams);
    }
}
