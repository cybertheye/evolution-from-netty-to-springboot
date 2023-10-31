package com.attackonarchitect.http;

import io.netty.handler.codec.http.HttpRequest;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import com.attackonarchitect.context.ServletContext;

import java.lang.reflect.Method;

/**
 * @description:
 */
public class HttpRequestProxy {

    private HttpRequest httpRequest;
    private ServletContext servletContext;

    HttpRequestProxy(HttpRequest httpRequest, ServletContext servletContext) {
        this.httpRequest = httpRequest;
        this.servletContext = servletContext;
    }

    public HttpMTRequest createRequest() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(HttpMTRequest.class);
        enhancer.setInterfaces(new Class[]{MTRequest.class});
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                Object result = null;
                try {
                    Method method0 = httpRequest.getClass().getMethod(method.getName(), method.getParameterTypes());
                    method0.setAccessible(true);
                    result = method0.invoke(httpRequest, args);
                }catch (NoSuchMethodException e){
                    result = proxy.invokeSuper(obj,args);
                }

                return result;
            }
        });

        return (HttpMTRequest) enhancer.create(new Class[]{ServletContext.class},new Object[]{servletContext});
    }
}
