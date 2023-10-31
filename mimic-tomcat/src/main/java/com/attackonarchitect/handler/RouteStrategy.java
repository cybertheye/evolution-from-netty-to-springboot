package com.attackonarchitect.handler;

import com.attackonarchitect.servlet.Servlet;

/**
 * @description:
 */

public interface RouteStrategy {
    Servlet route(String uri);
}
