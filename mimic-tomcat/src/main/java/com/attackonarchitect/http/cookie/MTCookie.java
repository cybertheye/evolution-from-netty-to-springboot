package com.attackonarchitect.http.cookie;

import com.attackonarchitect.utils.StringUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 描述 Http Cookie 类
 *
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2023/11/16
 * @since 1.8
 **/
public class MTCookie {
    /**
     * Cookie的名字
     */
    private String name;

    /**
     * Cookie的值
     */
    private String value;

    /**
     * Cookie的失效时间
     */
    private Date expires;

    /**
     * 有效期, 单位秒
     */
    private Integer maxAge;

    /**
     * 生效域名
     * 默认值为当前访问地址的host部分
     */
    private String domain;

    /**
     * 生效路径
     * 默认值为 /
     */
    private String path;

    /**
     * 是否仅HTTPS可用
     */
    private boolean secure;

    /**
     * 设置了HttpOnly属性的 cookie不能使用JavaScript访问
     */
    private boolean httpOnly;

    /**
     * 允许服务器设定 Cookie 不随着跨站请求一起发送
     */
    SameSite sameSite;

    /**
     * 允许特定条件跨域共享Cookie<br/>
     * <em>目前还在实验阶段</em><br/>
     * 还有一些额外要求:
     * <ul>
     * <li>SameParty Cookie 必须包含Secure.</li>
     * <li>SameParty Cookie 不得包含 SameSite=strict</li>
     * </ul>
     */
    boolean sameParty;

    /**
     * Cookie优先级
     * 只有Chrome实现了该提案
     */
    Priority priority;

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    public Date getExpires() {
        return expires;
    }

    void setExpires(Date expires) {
        this.expires = expires;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String getDomain() {
        return domain;
    }

    void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    void setPath(String path) {
        this.path = path;
    }

    public boolean isSecure() {
        return secure;
    }

    void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public SameSite getSameSite() {
        return sameSite;
    }

    void setSameSite(SameSite sameSite) {
        this.sameSite = sameSite;
    }

    public boolean isSameParty() {
        return sameParty;
    }

    void setSameParty(boolean sameParty) {
        this.sameParty = sameParty;
    }

    public Priority getPriority() {
        return priority;
    }

    void setPriority(Priority priority) {
        this.priority = priority;
    }

    MTCookie() {}

    private final static DateTimeFormatter expireDateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)
            .withZone(TimeZone.getTimeZone("GMT").toZoneId());

    @Override
    public String toString() {
        StringBuilder cookieValue = new StringBuilder();

        cookieValue.append(this.name).append('=').append(this.value).append("; ");

        // 失效时间
        if (Objects.nonNull(this.maxAge)) {
            if (Objects.nonNull(this.expires)) {
                System.err.printf("Cookie [%s=%s] 已经设置了 Max-Age, Expires 参数无效.%s",
                        this.name, this.value, System.lineSeparator());
            }
            cookieValue.append("max-age=").append(this.maxAge).append("; ");
        } else if (Objects.nonNull(this.expires)) {
            ZonedDateTime localDateTime = ZonedDateTime.ofInstant(this.expires.toInstant(), ZoneId.systemDefault());
            cookieValue.append("expires=").append(expireDateTimeFormatter.format(localDateTime)).append("; ");
        }

        // 作用域
        if (StringUtil.isNotBlank(this.domain)) {
            cookieValue.append("Domain=").append(this.domain).append("; ");
        }
        if (StringUtil.isNotBlank(this.path)) {
            cookieValue.append("Path=").append(this.path).append("; ");
        }

        // 布尔属性设置
        if (this.httpOnly) {
            cookieValue.append("HttpOnly; ");
        }
        if (this.secure) {
            cookieValue.append("Secure; ");
        }
        if (this.sameParty) {
            cookieValue.append("SameParty; ");
        }

        // 枚举属性设置
        cookieValue.append("SameSite=").append(Optional.ofNullable(this.sameSite)
                .orElseGet(SameSite::getDefault).getCode()).append("; ");
        cookieValue.append("Priority=").append(Optional.ofNullable(this.priority)
                .orElseGet(Priority::getDefault).getCode()).append("; ");

        return cookieValue.toString();
    }
}
