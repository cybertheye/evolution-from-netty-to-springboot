package com.attackonarchitect.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebFilter {
    /**
     * 拦截匹配上的uri
     */
    String[] value() default "/*";

    /**
     * 优先级,类似spring @Order 值越小优先级越高
     */
    int order() default 0;
}
