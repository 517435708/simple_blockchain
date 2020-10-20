package com.blackhearth.blockchain.p2ptests;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import lombok.SneakyThrows;

public class BasicClient {

    @SneakyThrows
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
        client.connect(5000, "localhost", 54555);

        Kryo kryo = client.getKryo();
        kryo.register(Response.class);

        Response request = new Response();
        request.setText("Here is the request");
        client.sendTCP(request);
    }
}
