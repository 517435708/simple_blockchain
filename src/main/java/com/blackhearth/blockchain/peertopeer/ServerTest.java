package com.blackhearth.blockchain.peertopeer;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class ServerTest {
    private ServerSocket server;

    public ServerTest(int port) throws
                                IOException {
        this.server = new ServerSocket(port);
    }

    public void runListener() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket receivedSocket = server.accept();
                    ObjectInputStream ois = new ObjectInputStream(receivedSocket.getInputStream());
                    String message = (String) ois.readObject();
                    log.info("Msg: {} received from {}:{}", message, receivedSocket.getInetAddress().getHostAddress(), receivedSocket.getPort());

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SneakyThrows
    public void send(String msg, String host, int port) {
        log.info("Sending {} to {}:{}", msg, host, port);
        Socket socket = new Socket(host, port);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(msg);
        oos.close();
        socket.close();
        log.info("Sent {} to {}:{}", msg, host, port);
    }
}
