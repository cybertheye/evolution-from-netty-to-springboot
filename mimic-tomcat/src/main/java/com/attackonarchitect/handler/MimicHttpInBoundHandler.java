package com.attackonarchitect.handler;

import com.attackonarchitect.listener.Notifier;
import com.attackonarchitect.listener.request.ServletRequestEvent;
import com.attackonarchitect.listener.request.ServletRequestListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import com.attackonarchitect.context.ServletContext;
import com.attackonarchitect.http.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 这里表示一个请求在web 应用中的最开端，表示初始化开始
 *
 */
public class MimicHttpInBoundHandler extends ChannelInboundHandlerAdapter {
    private ServletContext servletContext;

    public MimicHttpInBoundHandler(ServletContext servletContext) {
        this.servletContext = servletContext;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpRequest req  = (HttpRequest) msg;

        HttpMTRequest request = HttpRequestProxyFactory.createProxy(req,servletContext).createRequest();
        Map<String,String> parameters = new HashMap<>();

        this.parseParameters(req,parameters);
        request.setParametersDepot(parameters);
        request.parseHttpHeaders(req);

        ServletRequestEvent servletRequestEvent = new ServletRequestEvent();
        //todo set属性

        this.notifyRequestListener(servletRequestEvent);

        super.channelRead(ctx, request);
    }

    private void parseParameters(HttpRequest req, Map<String, String> parameters) {
        String uri = req.uri();
        // 需要支持没有参数
        if(!uri.contains("?")) return;


        String parametersPart = uri.split("\\?")[1];
        // name=cy&password=123456

        String[] paramParts = parametersPart.split("&");

        Arrays.stream(paramParts).forEach(pairParam->{
            String[] ps = pairParam.split("=");
            parameters.put(ps[0],ps[1]);
        });
    }

    private void notifyRequestListener(ServletRequestEvent sre) {
        Notifier notifier = (Notifier) servletContext.getAttribute("notifier");
        notifier.notifyListeners(ServletRequestListener.class,sre);
    }
}
