package netty.telnet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by 73681 on 2018/7/10.
 */
public class TelnetServer {
    private static final int PORT = 9999;
    private ServerBootstrap serverBootstrap;

    //一个被封装好的NIO线程池，bossGroup负责收集客户端连接
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    public void start() throws InterruptedException {
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.option(ChannelOption.SO_BACKLOG,1024);
        serverBootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ServerInitializer());
        Channel ch = serverBootstrap.bind(PORT).sync().channel();
        ch.closeFuture().sync();
    }

    public void close(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
