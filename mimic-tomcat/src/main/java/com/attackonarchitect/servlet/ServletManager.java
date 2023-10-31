package com.attackonarchitect.servlet;


import java.util.Map;
import java.util.Set;

/**
 * @description:
 */
public interface ServletManager {
    Servlet getSpecifedServlet(String uri);
    Map<String,Servlet> getAllServletMapping(boolean init);
    Set<String> getAllRequestUri();
}
