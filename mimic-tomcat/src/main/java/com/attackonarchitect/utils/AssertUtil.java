package com.attackonarchitect.utils;

import java.util.Objects;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2023/11/16
 * @since 1.8
 **/
public interface AssertUtil {
    static void isNotBlank(final CharSequence value, final String message) {
        if (!StringUtil.isNotBlank(value)) {
            throw new IllegalArgumentException(message);
        }
    }

    static void state(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }
}
