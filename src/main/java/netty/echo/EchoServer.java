package netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by 73681 on 2018/7/10.
 */
public class EchoServer {
    private static final int PORT = 9999;
    private ServerBootstrap serverBootstrap;




    public void start() throws Exception {

        //一个被封装好的NIO线程池，bossGroup负责收集客户端连接
        EventLoopGroup group = new NioEventLoopGroup(1);
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.option(ChannelOption.SO_BACKLOG,1024);
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)                  //指定使用 NIO 的传输 Channel
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {//添加 ServerHandler 到 Channel 的 ChannelPipeline
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new ServerHandler());
                        }
                    });
            ChannelFuture f = serverBootstrap.bind(PORT).sync();   //绑定的服务器;sync 等待服务器关闭
            f.channel().closeFuture().sync();                      //关闭 channel 和 块，直到它被关闭
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception{
        new EchoServer().start();
    }
}
