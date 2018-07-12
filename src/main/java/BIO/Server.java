package BIO;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * Created by 73681 on 2018/7/6.
 */
public class Server {
    private ServerSocket serverSocket;
    private short port;                     //服务器监听的端口号
    public Server(short port){
        this.port = port;
    }

    public void start(){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("服务器已启动" + "ip:"+ serverSocket.getInetAddress()+",端口号" + port);

            //接收客户点请求
            while (true){
                Socket socket = serverSocket.accept();

                //为每个请求创建一个线程
                new Thread(new ServerHandler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        //short port = (args[0] == null ? 9999 : Short.parseShort(args[0]));

        Server server = new Server((short)9999);
        server.start();
    }
}
