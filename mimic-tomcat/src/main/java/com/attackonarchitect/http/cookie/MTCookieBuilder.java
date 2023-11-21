package com.attackonarchitect.http.cookie;

import com.attackonarchitect.utils.AssertUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * {@linkplain MTCookie Cookie} 建造者
 *
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2023/11/16
 * @since 1.8
 **/
public class MTCookieBuilder {
    private final MTCookie cookie = new MTCookie();

    MTCookieBuilder() {}

    public MTCookieBuilder name(final String name) {
        cookie.setName(name);
        return this;
    }

    public MTCookieBuilder value(final String value) {
        cookie.setValue(value);
        return this;
    }

    public MTCookieBuilder domain(final String domain) {
        cookie.setDomain(domain);
        return this;
    }


    public MTCookieBuilder expires(final Date expires) {
        cookie.setExpires(expires);
        return this;
    }


    public MTCookieBuilder maxAge(final Integer maxAge) {
        cookie.setMaxAge(maxAge);
        return this;
    }

    public MTCookieBuilder path(final String path) {
        cookie.setPath(path);
        return this;
    }


    public MTCookieBuilder secure(final boolean secure) {
        cookie.setSecure(secure);
        return this;
    }

    public MTCookieBuilder secure() {
        return this.secure(true);
    }


    public MTCookieBuilder httpOnly(final boolean httpOnly) {
        cookie.setHttpOnly(httpOnly);
        return this;
    }

    public MTCookieBuilder httpOnly() {
        return this.httpOnly(true);
    }


    public MTCookieBuilder sameSite(final SameSite sameSite) {
        cookie.setSameSite(sameSite);
        return this;
    }

    public MTCookieBuilder sameParty(final boolean sameParty) {
        cookie.setSameParty(sameParty);
        return this;
    }

    public MTCookieBuilder sameParty() {
        return this.sameParty(true);
    }


    public MTCookieBuilder priority(final Priority priority) {
        cookie.setPriority(priority);
        return this;
    }

    public MTCookieBuilder expireAfter(long amount, TimeUnit timeUnit) {
        // 看与秒计算是否无误差
        long seconds = TimeUnit.SECONDS.convert(amount, timeUnit);
        if (amount == timeUnit.convert(seconds, TimeUnit.SECONDS)) {
            return this.maxAge(Math.toIntExact(seconds));
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MILLISECOND, Math.toIntExact(TimeUnit.MILLISECONDS.convert(amount, timeUnit)));
            return this.expires(calendar.getTime());
        }
    }

    private void check() {
        AssertUtil.isNotBlank(cookie.getName(), "请指定Cookie名称");
        AssertUtil.isNotBlank(cookie.getValue(), "请指定Cookie的值");

        if (cookie.isSameParty()) {
            // SameParty Cookie 必须包含Secure.
            AssertUtil.state(cookie.isSecure(), "SameParty Cookie 必须包含Secure.");

            // SameParty Cookie 不得包含 SameSite=strict
            AssertUtil.state(cookie.sameSite != SameSite.Strict, "SameParty Cookie 不得包含 SameSite=strict");
        }
    }

    public MTCookie build() {
        this.check();
        return cookie;
    }

    public static MTCookieBuilder newBuilder() {
        return new MTCookieBuilder();
    }
}
