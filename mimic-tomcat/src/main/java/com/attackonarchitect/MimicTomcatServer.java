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

import java.io.File;
import java.util.Objects;

/**
 * @description: 启动类
 * 使用netty-server 监听接收请求,作为Connector的简单替代实现
 */
public class MimicTomcatServer {
    private final int PORT;

    public MimicTomcatServer(int PORT) {
        this.PORT = PORT;
    }

    /**
     * 组件扫描器servlet,filter,listener
     */
    private ComponentScanner scanner;
    /**
     * servletContext
     * 1.配置信息
     * 2.全局数据共享
     */
    private ServletContext servletContext;
    public void start(Class<?> clazz){
        scanner = new WebComponentScanner(clazz);
        this.doStart();
    }

    /**
     * 依据配置文件初始化
     *
     * @param configFile 配置文件路径
     */
    public void start(String configFile) {
        if (configFile.endsWith(".xml")) {
            scanner = new XmlComponentScanner(configFile);
        } else {
            throw new UnsupportedOperationException("不支持的文件格式:  " + configFile);
        }
        this.doStart();
    }

    public void start(ClassLoader classLoader) {
        scanner = new SpiComponentScanner(classLoader);

        this.doStart();
    }

    private void doStart() {
        Notifier notifier = new NotifierImpl(Objects.requireNonNull(scanner, "没有找到合适的组件扫描器").getWebListenerComponents());
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
                            // 支持 http 协议
                            pipeline.addLast(new HttpServerCodec());
                            // 处理 http 长度较长导致的解析问题
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            // 传递上下文,事件监听注册
                            pipeline.addLast(new MimicHttpInBoundHandler(servletContext));
                            // 模拟servlet处理请求和响应
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
