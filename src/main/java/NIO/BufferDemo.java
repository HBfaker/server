package NIO;

import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by 73681 on 2018/7/9.
 */
public class BufferDemo {

    public static void printBufferInfo(Buffer buffer){
        System.out.println(buffer.position());          //读取和写入的下一个位置
        System.out.println(buffer.limit());             //可访问数据的末尾位置
        System.out.println(buffer.capacity());          //可保存元素的最大数目
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        printBufferInfo(buffer);//result:0,1024,1024

        buffer.position(64);
        buffer.limit(128);
        printBufferInfo(buffer);//result:64,128,1024

        //将position设置为0，并且limit设置capacity。表示缓冲区清空
        buffer.clear();
        printBufferInfo(buffer);//result:0,1024,1024

        //将postion变为0,limit不变
        buffer.position(64);
        buffer.limit(128);
        buffer.rewind();      //result:0,128,1024
        printBufferInfo(buffer);


        //填充和排空
        buffer.clear();
        buffer.put((byte)10);
        buffer.put((byte)11);
        printBufferInfo(buffer);//result:2,128,1024

//        //包装
//        byte[] data = "hello,world".getBytes("UTF-8");
//        ByteBuffer buffer1 = ByteBuffer.wrap(data);
//        printBufferInfo(buffer1);//result:0,11,11


    }
}
