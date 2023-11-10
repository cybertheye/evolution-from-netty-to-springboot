package com.attackonarchitect.webmvc.handler;

import java.util.Map;

/**
 * @description:
 */

public interface MimicHandlerMethodManager {
    Map<String,MimicHandlerMethod> getMethodMapping();

    MimicHandlerMethod getSpecifiedMethod(String uri);

}
