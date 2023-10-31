package com.attackonarchitect.servlet;

import com.attackonarchitect.context.ServletContext;
import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */
public abstract class MimicServlet implements Servlet{
    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    //TODO servletContext
    private ServletContext servletContext;
    @Override
    public void service(MTRequest req, MTResponse response) throws UnsupportedEncodingException {
        //
        String method = req.method().name();
        if("GET".equals(method)){
            doGet(req,response);
        } else if ("POST".equals(method)) {
            doPost(req,response);
        }

    }

    protected abstract void doPost(MTRequest req, MTResponse response);

    protected abstract void doGet(MTRequest req, MTResponse response) throws UnsupportedEncodingException;
}
