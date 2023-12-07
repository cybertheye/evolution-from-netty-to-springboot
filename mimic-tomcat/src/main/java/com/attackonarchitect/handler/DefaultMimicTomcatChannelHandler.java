package com.attackonarchitect.handler;

import com.attackonarchitect.filter.chain.FilterChain;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.attackonarchitect.ComponentScanner;
import com.attackonarchitect.context.ServletContext;
import com.attackonarchitect.filter.chain.FilterChainImplFactory;
import com.attackonarchitect.http.HttpMTResponse;
import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;
import com.attackonarchitect.servlet.Servlet;
import com.attackonarchitect.servlet.ServletManagerFactory;

/**
 * @description:
 */

public class DefaultMimicTomcatChannelHandler extends ChannelInboundHandlerAdapter {

    private ComponentScanner scanner;
    private ServletContext servletContext;
    public DefaultMimicTomcatChannelHandler(ComponentScanner scanner, ServletContext servletContext) {
        this.scanner = scanner;
        this.servletContext = servletContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MTRequest request = (MTRequest) msg;
        String uri = request.uri();

        RouteStrategy strategy = new RouteMaxMatchStrategy(ServletManagerFactory.getInstance(scanner,servletContext));
        Servlet servlet = strategy.route(uri);

        MTResponse response = new HttpMTResponse(ctx);

        FilterChain filterChain = FilterChainImplFactory.createFilterChain(servlet,uri,scanner);

        filterChain.doFilter(request,response);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }
}
