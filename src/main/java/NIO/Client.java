package NIO;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by 73681 on 2018/7/6.
 */
public class Client {
    private ClientHandler clientHandler;
    private String serverIp;             //服务器IP
    private short serverPort;            //服务器端口号

    public Client(String serverIp, short serverPort){
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void stop(){
        clientHandler.stop();
    }

    private void start(){
        clientHandler = new ClientHandler(serverPort,serverIp);
        //客户端线程启动
        new Thread(clientHandler,"client").start();
    }

    //发送消息数据
    public boolean send(String data) throws Exception {
        if (data.equals("q")) return false;
        clientHandler.send(data);
        return true;
    }


    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        short port = 9999;
        Client client = new Client(host,port);

        client.start();
        while(client.send(new Scanner(System.in).nextLine()));

        client.stop();
    }
}
