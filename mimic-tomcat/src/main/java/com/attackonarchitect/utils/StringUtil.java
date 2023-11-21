package com.attackonarchitect.utils;

import java.util.Objects;

/**
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2023/11/16
 * @since 1.8
 **/
public interface StringUtil {
    static boolean isNotBlank(final CharSequence value) {
        return Objects.nonNull(value) &&
                !value.chars().allMatch(Character::isWhitespace);
    }
}
