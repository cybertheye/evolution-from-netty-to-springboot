package com.attackonarchitect.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import com.attackonarchitect.context.ServletContext;
import com.attackonarchitect.http.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
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
        this.notifyRequestListener();

        super.channelRead(ctx, request);
    }

    private void parseParameters(HttpRequest req, Map<String, String> parameters) {
        String uri = req.uri();
        String parametersPart = uri.split("\\?")[1];
        // name=cy&password=123456

        String[] paramParts = parametersPart.split("&");

        Arrays.stream(paramParts).forEach(pairParam->{
            String[] ps = pairParam.split("=");
            parameters.put(ps[0],ps[1]);
        });
    }

    private void notifyRequestListener() {
    }
}
