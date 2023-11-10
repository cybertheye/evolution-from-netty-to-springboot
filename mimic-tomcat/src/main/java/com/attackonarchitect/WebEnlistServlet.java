package com.attackonarchitect;

import com.attackonarchitect.servlet.WebInitParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 *
 * 用来支持 Spring MVC的 DispatcherServlet 注册注入 Tomcat 中。
 * 模拟 Spring MVC中 用xml 去配置的形式
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebEnlistServlet {
    String servletName();
    String servletClass();

    String[] urlPattern();

    int loadOnStartup() default -1;

    WebInitParam[] initParams() default {};
}
