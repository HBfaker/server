package netty.telnet;

/**
 * Created by 73681 on 2018/7/10.
 */
public class Main {
    public static void main(String[] args){
        TelnetServer server = new TelnetServer();
        try {
            server.start();
        } catch (InterruptedException e) {
            server.close();
        }
    }
}
