package com.attackonarchitect.servlet;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */
public class DefaultMimicServlet extends MimicServlet{
    @Override
    protected void doPost(MTRequest req, MTResponse response) {

    }

    @Override
    protected void doGet(MTRequest req, MTResponse response) throws UnsupportedEncodingException {
        response.write("this is default servlet. request is wrong if you see this");
    }
}
