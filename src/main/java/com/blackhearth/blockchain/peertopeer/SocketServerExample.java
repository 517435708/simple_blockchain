package com.blackhearth.blockchain.peertopeer;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class implements java Socket server
 * @author pankaj
 *
 */
public class SocketServerExample {
    private static int port = 9876;
    private static String receiverHost = "192.168.0.100";
    private static int receiverPort = 6789;


    public static void main(String[] args) throws IOException, ClassNotFoundException{
        ServerTest serverTest = new ServerTest(port);

        serverTest.runListener();

        while (true) {
            int randomNum = ThreadLocalRandom.current().nextInt(1, 100 + 1);

            if (randomNum < 5) {
                serverTest.send("Dupa", receiverHost, receiverPort);
            }
        }
    }

}
