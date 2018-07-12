package NIO;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created by 73681 on 2018/7/6.
 * 服务端用来处理客户端请求的类
 */
public class ServerHandler implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private short port;
    private volatile boolean isStart = false;

    public ServerHandler(short port){
        this.port = port;

        try {
            //创建选择器
            selector = Selector.open();
            //打卡监听通道
            serverSocketChannel = ServerSocketChannel.open();
            //绑定监听端口号，backlog是什么鬼？
            serverSocketChannel.bind(new InetSocketAddress(port),1204);
            //将通道设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            //将这个监听通道注册到选择器上去
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //标记服务器已经开启
            isStart = true;
            System.out.println("服务器已启动");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while (isStart){

            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys =  selector.selectedKeys();
                Iterator<SelectionKey> itrSelectionKey = selectionKeys.iterator();
                SelectionKey key = null;
                while (itrSelectionKey.hasNext()){
                   key = itrSelectionKey.next();
                   itrSelectionKey.remove();
                    handlerInput(key);
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    private void handlerInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            //处理请求消息
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }else if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                //创建一个缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                //将通道里的数据读到缓冲区中
                int readBytes = sc.read(buffer);

                if (readBytes > 0) {
                    buffer.flip();
                    byte[] datas = new byte[buffer.remaining()];
                    //将缓存区的可读字节复制到字节数组中
                    buffer.get(datas);
                    String str = new String(datas,"UTF-8");
                    //根据缓冲区可读数据，创建字节数组

                    //发送消息
                    String resp = "I am server," + "now is " + new Date() + "-----------I receive:" + str;
                    byte[] bytes = resp.getBytes("UTF-8");
                    ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                    writeBuffer.put(bytes);
                    writeBuffer.flip();
                    sc.write(writeBuffer);

                } else {
                    key.cancel();
                    sc.close();
                }
            }else if (key.isWritable()){
                SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer buffer = (ByteBuffer)key.attachment();

            }
        }
    }
}
