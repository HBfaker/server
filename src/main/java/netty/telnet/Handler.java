package netty.telnet;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;

/**
 * Created by 73681 on 2018/7/10.
 */
public class Handler extends SimpleChannelInboundHandler<String>{

    protected void channelRead0(ChannelHandlerContext ctx, String req) throws Exception {
        String response;
        boolean close =false;
        if(req.isEmpty()){
            response = "please type something.\r\n";
        }else if (req.equals("exit")){
            response = "bye,bye!\r\n";
        }else{
            response = "Did you say '" + req + "'?\r\n";
        }

        ChannelFuture future = ctx.write(response);
        ctx.flush();
        if (close){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.write("welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
