package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.protocol.interpreter.ProtocolInterpreter;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class InterpreterBlockChainCommunication implements BlockChainCommunication {
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int PRINT_ALL_NODES_SEQUENCE_MS = 50000;

    private final Server server;
    private final Client client;
    private final ProtocolInterpreter interpreter;
    private final BasicPeerToPeerRepository repository;
    private int serverPort;

    public InterpreterBlockChainCommunication(Server server,
                                              Client client,
                                              @Lazy ProtocolInterpreter interpreter,
                                              BasicPeerToPeerRepository repository) {
        this.server = server;
        this.client = client;
        this.interpreter = interpreter;
        this.repository = repository;
    }

    @Override
    public void start(int tcpPort) throws IOException {
        server.start();
        server.bind(tcpPort);
        serverPort = tcpPort;
        client.start();
        initializeInterpreter();

        printInfoAboutKnownHosts();
    }

    @Override
    public void sendTo(String message, String host, int port) throws IOException {
        client.connect(CONNECTION_TIMEOUT, host, port);
        client.sendTCP(new CommunicationObject(message, serverPort));
    }

    @Override
    public List<BlockChainNodeData> getAllKnownHosts() {
        return repository.getNodes();
    }

    @Override
    public void closeConnections() {
        server.close();
        client.close();
    }

    @PostConstruct
    private void registerCommunicationClass() {
        log.debug("Registered communictaion class");
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
        String port = String.valueOf(object.getSenderPort());
        String text = object.getText();

        log.debug("Interpreting: {} from {}:{}", text, hostAddress, port);
        interpreter.interpretMessage(text, hostAddress, port);
    }

    private void printInfoAboutKnownHosts() {
        new Thread(() -> {
            while (true) {
                repository.getNodes();

                try {
                    Thread.sleep(PRINT_ALL_NODES_SEQUENCE_MS);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        });
    }
}

