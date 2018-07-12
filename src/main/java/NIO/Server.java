package NIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Selector;

/**
 * Created by 73681 on 2018/7/6.
 */
public class Server {
    private short port;                     //服务器监听的端口号
    private ServerHandler serverHandler;

    public Server(short port){
        this.port = port;
    }

    //启动服务器处理线程
    public void start(){
        serverHandler = new ServerHandler(port);
        new Thread(serverHandler,"server").start();
    }


    public static void main(String[] args){
        short port = 9999;
        new Server(port).start();
    }
}
