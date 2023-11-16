package com.attackonarchitect.http;

import com.attackonarchitect.http.cookie.MTCookie;
import com.attackonarchitect.http.cookie.MTCookieBuilder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @description:
 */
public class HttpMTResponse implements MTResponse{
    private ChannelHandlerContext ctx;

    private final Map<String, String> headers = new HashMap<>();

    private final Map<String, MTCookie> cookieMap = new HashMap<>();

    public HttpMTResponse(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }


    private StringBuilder contents = new StringBuilder();

    @Override
    public void write(String content)  {
        contents.append(content).append(System.lineSeparator());
    }


    // 如何把这个方法设置为内部接口？？？
    @Override
    public void flush() throws UnsupportedEncodingException {
        DefaultFullHttpResponse response =
                new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        Unpooled.copiedBuffer(contents.toString().getBytes("UTF-8"))
                );

        HttpHeaders headers = response.headers();
        headers.set(HttpHeaderNames.CONTENT_TYPE, Optional.ofNullable(headers.get(HttpHeaderNames.CONTENT_TYPE)).orElse("text/plain;charset=utf-8"));
        headers.set(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());


        for (MTCookie cookie : cookieMap.values()) {
            headers.add(HttpHeaderNames.SET_COOKIE, cookie.toString());
        }

        for (Map.Entry<String, String> header : this.headers.entrySet()) {
            if (headers.contains(header.getKey())) {
                System.err.println("已经设置的响应消息头: " + header.getKey());
            } else {
                headers.set(header.getKey(), header.getValue());
            }
        }

        ctx.writeAndFlush(response);
    }

    @Override
    public void addHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

    @Override
    public void setCookie(final String cookieName, final String cookieValue) {
        MTCookie cookie = MTCookieBuilder.newBuilder()
                .name(cookieName).value(cookieValue)
                .build();
        this.cookieMap.put(cookieName, cookie);
    }

    @Override
    public void setCookie(final String cookieName, final MTCookie cookie) {
        this.cookieMap.put(cookieName, cookie);
    }

    @Override
    public void setCookie(final MTCookie cookie) {
        this.cookieMap.put(Objects.requireNonNull(cookie, "请生成一个Cookie").getName(), cookie);
    }
}
