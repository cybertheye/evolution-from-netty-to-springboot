package com.attackonarchitect;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 */

public interface ComponentScanner {

    /**
     * 获取到所有的WebListener的类名
     * @return
     */
    List<String> getWebListenerComponents();


    /**
     * 一对多
     * 获取到所有的WebServlet的类名
     * @return /* -> servletName
     */
    Map<String, String> getWebServletComponents();


    /**
     * 多对多
     * 获取到所有的WebFilter的类名以及映射关系
     * @return {/*,/a} ->  filterName
     */
    Map<String,Set<String>> getWebFilterComponents();

}
