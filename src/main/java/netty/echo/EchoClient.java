package netty.echo;

import io.netty.bootstrap.Bootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;

/**
 * Created by 73681 on 2018/7/11.
 */
public class EchoClient {
    private final String host;
    private final short port;

    public EchoClient(String host,short port){
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        //一个被封装好的NIO线程池，处理客户端事件
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG,1024);
            bootstrap.group(group)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .channel(NioSocketChannel.class)                  //指定使用 NIO 的传输 Channel
                    .handler(new ChannelInitializer<SocketChannel>() {//添加 ServerHandler 到 Channel 的 ChannelPipeline
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new ClientHandler());
                        }
                    });
            ChannelFuture f = bootstrap.connect().sync();          //连接到远程等到连接完成
            f.channel().closeFuture().sync();                      //阻塞直到channel关闭
        }finally {
            group.shutdownGracefully().sync();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        final String host = "127.0.0.1";
        final short port = 9999;

        new EchoClient(host,port).start();
    }
}
