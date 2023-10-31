package com.attackonarchitect.servlet;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */
public class ErrorMimicServlet extends MimicServlet{
    @Override
    protected void doPost(MTRequest req, MTResponse response) {

    }

    @Override
    protected void doGet(MTRequest req, MTResponse response) throws UnsupportedEncodingException {
        response.write("something wrong");
    }
}
