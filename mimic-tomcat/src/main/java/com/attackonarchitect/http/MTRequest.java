package com.attackonarchitect.http;

import io.netty.handler.codec.http.HttpRequest;
import com.attackonarchitect.context.ServletContext;

import java.util.Map;

/**
 * @description:
 */

public interface MTRequest extends HttpRequest {
    ServletContext getServletContext();
    public Object getAttribute(String name);

    public void setAttribute(String name, Object obj);


    Map<String,String> getParameters();
    String getParameter(String name);
}
