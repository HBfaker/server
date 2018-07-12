package BIO;

import java.util.Scanner;

/**
 * Created by 73681 on 2018/7/7.
 */
public class Main {
    public static void main(String[] args){
        String serverIp = "127.0.0.1";
        short serverPort = 9999;
        Client client = new Client(serverIp,serverPort);

        Scanner scanner = new Scanner(System.in);

        while (true){
            String str = scanner.next();
            client.send(str);

            if (str.equals("exit")) break;
        }
    }
}
