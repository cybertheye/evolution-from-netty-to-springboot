package com.attackonarchitect;

import com.attackonarchitect.servlet.ServletInformation;

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
    @Deprecated
    Map<String, String> getWebServletComponents();


    /**
     * 代替getWebServletComponents
     * /* -> servletinformation 键值对
     * @return
     */
    Map<String, ServletInformation> getServletInformationMap();

    /**
     * 多对多
     * 获取到所有的WebFilter的类名以及映射关系
     * @return {/*,/a} ->  filterName
     */
    Map<String,Set<String>> getWebFilterComponents();

    /**
     * 记录不同 filter 之间的执行顺序
     * @return
     */
    Map<String,Integer> getWebFilterComponentsOrder();

}
