package com.attackonarchitect.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */
public class HttpMTResponse implements MTResponse{
    private ChannelHandlerContext ctx;

    public HttpMTResponse(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }


    private StringBuilder contents = new StringBuilder();

    @Override
    public void write(String content)  {
        contents.append(content).append(System.lineSeparator());
    }

    @Override
    public void writeAndFlush(String content) throws UnsupportedEncodingException {
        this.write(content);
        DefaultFullHttpResponse response =
                new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK,
                        Unpooled.copiedBuffer(contents.toString().getBytes("UTF-8"))
                );

        HttpHeaders headers = response.headers();
        headers.set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
        headers.set(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());

        ctx.writeAndFlush(response);
    }
}
