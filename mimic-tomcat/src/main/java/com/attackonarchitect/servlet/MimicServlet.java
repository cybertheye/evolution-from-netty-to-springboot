package com.attackonarchitect.servlet;

import com.attackonarchitect.context.ServletContext;
import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */
public abstract class MimicServlet implements Servlet{

    //TODO servletContext
    private ServletContext servletContext;
    @Override
    public void service(MTRequest req, MTResponse response) throws UnsupportedEncodingException {
        //
        String method = req.method().name();
        if("GET".equals(method)){
            doGet0(req,response);
        } else if ("POST".equals(method)) {
            doPost0(req,response);
        }

    }

    private void doPost0(MTRequest req, MTResponse response) throws UnsupportedEncodingException {
        doPost(req,response);
        response.flush();
    }

    private void doGet0(MTRequest req, MTResponse response) throws UnsupportedEncodingException {
        doGet(req,response);
        response.flush();
    }


    protected abstract void doPost(MTRequest req, MTResponse response);

    protected abstract void doGet(MTRequest req, MTResponse response) throws UnsupportedEncodingException;


    /////getter ,setter


    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }


}
