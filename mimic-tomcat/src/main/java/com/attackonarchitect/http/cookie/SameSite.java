package com.attackonarchitect.http.cookie;

/**
 * {@link MTCookie} 的 SameSite 属性值
 * 之前默认是{@link SameSite#None}的, Chrome80后默认是{@link SameSite#Lax}
 *
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2023/11/16
 * @since 1.8
 * @see MTCookie#sameSite
 **/
public enum SameSite {
    /**
     * 仅允许一方请求携带Cookie, 即浏览器将只发送相同站点请求的Cookie, 即当前网页URL与请求目标URL完全一致
     */
    Strict("strict"),

    /**
     * 允许部分第三方请求携带Cookie
     */
    Lax("lax"),

    /**
     * 无论是否跨站都会发送Cookie
     */
    None("none");

    private final String code;


    SameSite(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * 当前默认值
     * @return
     */
    public static SameSite getDefault() {
        return Lax;
    }
}
