package com.attackonarchitect;

import com.attackonarchitect.servlet.ServletManagerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import com.attackonarchitect.context.ServletContext;
import com.attackonarchitect.context.ServletContextFactory;
import com.attackonarchitect.handler.DefaultMimicTomcatChannelHandler;
import com.attackonarchitect.handler.MimicHttpInBoundHandler;
import com.attackonarchitect.listener.Notifier;
import com.attackonarchitect.listener.NotifierImpl;

/**
 * @description:
 */
public class MimicTomcatServer {
    private final int PORT;

    public MimicTomcatServer(int PORT) {
        this.PORT = PORT;
    }


    private ComponentScanner scanner;
    private ServletContext servletContext;
    public void start(Class<?> clazz){

        scanner = new WebComponentScanner(clazz);
        Notifier notifier = new NotifierImpl(scanner.getWebListenerComponents());
        servletContext = ServletContextFactory.getInstance(scanner.getWebListenerComponents(),notifier);
        servletContext.setAttribute("notifier",notifier);

        //初始化servlet一下，主要是preinit
        ServletManagerFactory.getInstance(scanner,servletContext);

        runNetty();
    }

    private void runNetty(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(new MimicHttpInBoundHandler(servletContext));
                            pipeline.addLast(new DefaultMimicTomcatChannelHandler(scanner,servletContext));

                        }
                    });

            ChannelFuture cf = serverBootstrap.bind(PORT).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
