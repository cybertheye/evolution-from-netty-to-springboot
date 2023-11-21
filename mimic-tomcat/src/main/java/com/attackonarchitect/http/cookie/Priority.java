package com.attackonarchitect.http.cookie;

/**
 * 优先级
 * <p><em><b>目前只有Chrome实现了该提案</b></em></p>
 * <p>Cookie有数量限制, 所以在Cookie超过一定数量时, 浏览器会清除最早过期的Cookie.</p>
 * <p>如果设置了 Priority, 浏览器会将优先级低的Cookie先清除, 并且每种优先级Cookie至少保留一个.</p>
 *
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2023/11/16
 * @since 1.8
 **/
public enum Priority {
    /**
     * 低优先级
     */
    Low("Low"),
    /**
     * 中优先级
     */
    Medium("Medium"),
    /**
     * 高优先级
     */
    High("High");

    private final String code;

    Priority(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * 当前默认值
     * @return
     */
    public static Priority getDefault() {
        return Medium;
    }
}
