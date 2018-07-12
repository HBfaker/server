package BIO;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * Created by 73681 on 2018/7/6.
 * 服务端用来处理客户端请求的类
 */
public class ServerHandler implements Runnable {

    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public ServerHandler(Socket socket){
        this.socket = socket;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        handler();
    }

    private void handler(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        PrintWriter writer = new PrintWriter(out,true);

        try {
            //用于接收客户端传过来的数据
            String str;
            //从输入流里面读一行，没有读到就阻塞，所以是阻塞性IO
            while ((str = reader.readLine()) != null){
                if (str.equals("exit")){
                    break;
                }
                //服务器传给客户端
                writer.println("I am server," +"now is " + new Date() +
                        "-----------I receive:" + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
    }
}
