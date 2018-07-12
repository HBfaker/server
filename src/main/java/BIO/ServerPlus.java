package BIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 73681 on 2018/7/9.
 * 使用线程池来处理
 */
public class ServerPlus {
    private ServerSocket serverSocket;
    private short port;                     //服务器监听的端口号
    private ExecutorService executor;
    public ServerPlus(short port){
        this.port = port;
        executor =  Executors.newFixedThreadPool(100);
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("服务器已启动" + "ip:"+ serverSocket.getInetAddress()+",端口号" + port);
        while (true){
            Socket socket = serverSocket.accept();
            //将处理任务提交给线程池
            executor.submit(new ServerHandler(socket));
        }
    }

    public static void main(String[] args) throws IOException {
        short port = 9999;
        ServerPlus server = new ServerPlus(port);
        server.start();
    }

}
