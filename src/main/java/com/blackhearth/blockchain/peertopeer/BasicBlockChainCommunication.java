package com.blackhearth.blockchain.peertopeer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BasicBlockChainCommunication implements BlockChainCommunication {
    private static final int CONNECTION_TIMEOUT = 5000;
    private Server server;
    private Client client;

    public BasicBlockChainCommunication() {
        this.server = new Server();
        this.client = new Client();
        registerCommunicationClass();
    }

    @Override
    public void start(int tcpPort) throws IOException {
        server.start();
        server.bind(tcpPort);
    }

    @Override
    public void addListenerOnReceived(Runnable toRunOnReceived) {
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof CommunicationObject) {
                    // TODO: 21.10.2020 obsługiwanie zapytać, ale nie
                    // wiem co powinno tej metody uzyć
                    log.info("Received: {}", ((CommunicationObject) object).getText());
                    toRunOnReceived.run(); // todo - a może po prostu metoda, która obsługuje zapytania???
                }
            }
        });
    }

    @Override
    public void sendTo(String message, String host, int port) throws IOException {
        client.start();
        client.connect(CONNECTION_TIMEOUT, host, port);
        client.sendTCP(new CommunicationObject(message));

        client.close();
    }

    @Override
    public void closeConnections() {
        server.close();
        client.close();
    }

    private void registerCommunicationClass() {
        Kryo serverKryo = server.getKryo();
        serverKryo.register(CommunicationObject.class);

        Kryo clientKryo = client.getKryo();
        clientKryo.register(CommunicationObject.class);
    }
}

