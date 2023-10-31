package com.attackonarchitect.servlet;


import com.attackonarchitect.context.ServletContext;
import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */

public interface Servlet {
    public void service(MTRequest req, MTResponse response) throws UnsupportedEncodingException;

    ServletContext getServletContext();
}
