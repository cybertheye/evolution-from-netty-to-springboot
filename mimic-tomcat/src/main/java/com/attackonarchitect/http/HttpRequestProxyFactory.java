package com.attackonarchitect.http;

import io.netty.handler.codec.http.HttpRequest;
import com.attackonarchitect.context.ServletContext;

/**
 * @description:
 */
public class HttpRequestProxyFactory {
    private HttpRequestProxyFactory(){}


    public static HttpRequestProxy createProxy(HttpRequest request, ServletContext servletContext){
        return new HttpRequestProxy(request,servletContext);
    }
}
