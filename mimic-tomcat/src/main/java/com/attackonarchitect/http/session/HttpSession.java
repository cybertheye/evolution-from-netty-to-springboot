package com.attackonarchitect.http.session;

import com.attackonarchitect.context.ServletContext;

import java.util.Enumeration;
import java.util.jar.Attributes;

/**
 * @author xiong
 * @date 2023/11/29 15:42
 */
public interface HttpSession {
    /**
     * 获取SessionId
     * @return
     */
    String getSessionId();


    /**
     * 获取Session创建时间
     * @return
     */
    long getCreationTime();

    /**
     * 获取Session最后访问时间
     * @return
     */
    long getLastAccessedTime();

    void setLastAccessedTime(long lastAccessedTime);


    /**
     * 获取Session最大存活时间
     * @return
     */
    int getMaxInactiveInterval();

    /**
     * 获取Session属性
     * @return
     */
    Attributes getAttributes();


}
