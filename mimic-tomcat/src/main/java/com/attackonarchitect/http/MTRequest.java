package com.attackonarchitect.http;

import com.attackonarchitect.http.session.HttpSession;
import io.netty.handler.codec.http.HttpRequest;
import com.attackonarchitect.context.ServletContext;

import java.util.Iterator;
import java.util.List;
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

    /**
     * 获取请求头的值
     * @param name
     * @return
     */
    String getHeader(final String name);

    /**
     * 获取请求头的值列表
     * @param name
     * @return
     */
    List<String> getHeaders(final String name);

    /**
     * 获取所有请求头的名字
     *
     * @return
     */
    Iterator<String> getHeaderNames();

    /**
     * 获取指定cookie 的值
     *
     * @param cookie
     * @return
     */
    String getCookie(final String cookie);

    /**
     * 获取所有cookie的迭代器
     * @return
     */
    Iterator<String> getCookieNames();

    /**
     * 获取Session
     * @return
     */
    HttpSession getSession();
}
