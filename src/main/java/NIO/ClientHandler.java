package NIO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by 73681 on 2018/7/8.
 */
public class ClientHandler implements Runnable{

    private short serverPort;
    private String serverIp;
    private SocketChannel socketChannel;
    private Selector selector;
    private volatile boolean isStarted = false;
    public ClientHandler(short serverPort, String serverIp){
        this.serverIp = serverIp;
        this.serverPort = serverPort;

        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            isStarted = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        isStarted = false;
    }

    @Override
    public void run() {
        try {
            //首先连接服务器
            doConnent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //循环遍历selector
        while (isStarted){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys =  selector.selectedKeys();
                Iterator<SelectionKey> itrSelectionKey = selectionKeys.iterator();
                SelectionKey key = null;
                while (itrSelectionKey.hasNext()){
                    key = itrSelectionKey.next();
                    itrSelectionKey.remove();
                    handleInput(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()){
            SocketChannel sc = (SocketChannel)key.channel();
            if (key.isConnectable()){
                sc.finishConnect();
                sc.register(selector,SelectionKey.OP_READ);
            } else if (key.isReadable()){
                //创建一个缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                //将通道里的数据读到缓冲区中
                int readBytes = sc.read(buffer);

                if (readBytes > 0) {
                    //？
                    buffer.flip();
                    //根据缓冲区可读数据，创建字节数组
                    byte[] datas = new byte[buffer.remaining()];

                    buffer.get(datas);
                    //收到的数据
                    String str = new String(datas, "UTF-8");
                    System.out.println(str);

                }else if (readBytes < 0){
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doConnent() throws IOException {
        socketChannel.connect(new InetSocketAddress(serverIp,serverPort));
        socketChannel.register(selector,SelectionKey.OP_CONNECT);
    }



    public void send(String data) throws Exception {
        socketChannel.register(selector,SelectionKey.OP_READ);
        byte[] bytes = data.getBytes("UTF-8");
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
    }
}
