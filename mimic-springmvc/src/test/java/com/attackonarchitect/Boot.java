package com.attackonarchitect;

import com.attackonarchitect.servlet.WebInitParam;

/**
 * @description:
 * 1. 怎么注册 DispatcherServlet
 * 2. 怎么和 Spring 进行结合。Spring的扫描路径
 */

@WebEnlistServlet(
        servletName = "dispatcherServlet",
        servletClass = "com.attackonarchitect.webmvc.servlet.DispatcherServlet",
        urlPattern = "/",
        loadOnStartup = 1,
        initParams = {
                @WebInitParam(name = "contextConfigLocation",value = "com.attackonarchitect.controller")
        }
)
public class Boot {
    public static void main(String[] args) {
        new MimicTomcatServer(9999).start(Boot.class);
    }
}
