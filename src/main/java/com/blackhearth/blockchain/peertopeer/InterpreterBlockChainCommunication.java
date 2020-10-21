package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.protocol.interpreter.ProtocolInterpreter;
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
public class InterpreterBlockChainCommunication implements BlockChainCommunication {
    private static final int CONNECTION_TIMEOUT = 5000;
    private Server server;
    private Client client;

    // TODO: 21.10.2020 initialize
    private ProtocolInterpreter interpreter;

    public InterpreterBlockChainCommunication() {
        this.server = new Server();
        this.client = new Client();
        registerCommunicationClass();
    }

    @Override
    public void start(int tcpPort) throws IOException {
        server.start();
        server.bind(tcpPort);
        initializeInterpreter();
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

    private void initializeInterpreter() {
        server.addListener(new Listener() {
            @Override
            public void received (Connection connection, Object object) {
                if (object instanceof CommunicationObject) {
                    delegateToInterpretMessage(connection, (CommunicationObject) object);
                }
            }
        });
    }

    private void delegateToInterpretMessage(Connection connection, CommunicationObject object) {
        String hostAddress = connection.getRemoteAddressTCP().getAddress().getHostAddress();
        String port = String.valueOf(connection.getRemoteAddressTCP().getPort());
        String text = object.getText();

        log.info("Interpreting: {} from {}:{}", text, hostAddress, port);
        interpreter.interpretMessage(text, hostAddress, port);
    }
}

