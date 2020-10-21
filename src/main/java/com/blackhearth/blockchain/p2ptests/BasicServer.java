package com.blackhearth.blockchain.p2ptests;

import com.blackhearth.blockchain.peertopeer.CommunicationObject;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.stream.Stream;

public class BasicServer {

    @SneakyThrows
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
        server.bind(54555);

        Kryo kryo = server.getKryo();
        kryo.register(CommunicationObject.class);

        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof CommunicationObject) {
                    CommunicationObject request = (CommunicationObject)object;
                    System.out.println(request.getText());

                    Object[] objects = Stream.of(server.getConnections())
                            .map(Connection::getRemoteAddressTCP)
                            .toArray();

                    System.out.println(Arrays.toString(objects));
                }
            }
        });

    }
}
