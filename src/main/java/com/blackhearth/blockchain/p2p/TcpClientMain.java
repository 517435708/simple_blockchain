package com.blackhearth.blockchain.p2p;

import com.esotericsoftware.kryonet.Client;
import lombok.SneakyThrows;

public class TcpClientMain {
    @SneakyThrows
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
        client.connect(5000, "localhost", 54555, 54777);

        SomeRequest request = new SomeRequest();
        request.text = "Here is the request";
        client.sendTCP(request);
    }
}