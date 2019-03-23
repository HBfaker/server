package BIO;

import com.sun.org.apache.regexp.internal.RE;

import java.io.*;
import java.net.Socket;
import java.lang.Thread;
/**
 * Created by 73681 on 2018/7/6.
 */
public class Client {
    private Socket socket;
    private String serverIp;             //服务器IP
    private short serverPort;            //服务器端口号
    private InputStream in;
    private OutputStream out;

    public Client(String serverIp,short serverPort){
        try {
            this.serverIp = serverIp;
            this.serverPort = serverPort;
            socket = new Socket(serverIp,serverPort);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void close(){
        //关闭socket
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //关闭流
        if (in != null){
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (out != null){
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    //向服务器发送数据
    public void send(String data){
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        PrintWriter writer = new PrintWriter(out,true);
        try {

            //发送exit,就关闭socket连接
            if (data.equals("exit")){
                close();
                return;
            }

            //向服务器发送数据
            writer.println(data);

            String str;
            str = reader.readLine();
            System.out.println(str);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        //模拟多个客户端
        int clinetNums = 10;
        String serverIp = "127.0.0.1";
        short serverPort = 9999;

        //final Client client = new Client(serverIp,serverPort);
        //client.send("I am client");
        for (int i = 0; i < clinetNums; i++){
            final int num = i;
            final Client client = new Client(serverIp,serverPort);
            new Thread(new Runnable() {
                public void run() {
                    client.send("I am client-" + num);
                }
            }).start();
        }
    }
}
