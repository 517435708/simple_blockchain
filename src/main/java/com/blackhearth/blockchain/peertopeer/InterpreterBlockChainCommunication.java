package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.protocol.interpreter.ProtocolInterpreter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

@Slf4j
@Component
public class InterpreterBlockChainCommunication implements BlockChainCommunication {
    private ServerSocket server;
    private final ProtocolInterpreter interpreter;
    private final BasicPeerToPeerRepository repository;

    public InterpreterBlockChainCommunication(@Lazy ProtocolInterpreter interpreter,
                                              BasicPeerToPeerRepository repository) {
        this.interpreter = interpreter;
        this.repository = repository;
    }

    @Override
    public void start(int tcpPort) throws IOException {
        this.server = new ServerSocket(tcpPort);
        initializeInterpreter();
    }

    @Override
    public void sendTo(String message, String host, int port) {
        try {
            log.info("Sending {} to {}:{}", message, host, port);
            Socket socket = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            oos.close();
            socket.close();
            log.info("Sent {} to {}:{}", message, host, port);
        }
        catch (Exception e) {
            log.error("Sending {} to {}:{} failed", message, host, port);
        }
    }

    @Override
    public Set<BlockChainNodeData> getAllKnownHosts() {
        return repository.getNodes();
    }

    @Override
    @SneakyThrows
    public void closeConnections() {
        server.close();
    }

    private void initializeInterpreter() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket receivedSocket = server.accept();
                    ObjectInputStream ois = new ObjectInputStream(receivedSocket.getInputStream());
                    String message = (String) ois.readObject();
                    delegateToInterpretMessage(receivedSocket.getInetAddress().getHostAddress(), receivedSocket.getPort(), new CommunicationObject(message));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void delegateToInterpretMessage(String hostAddress, int port, CommunicationObject object) {
        String msg = object.getText();
        log.info("Interpreting: {} from {}:{}", msg, hostAddress, port);
        interpreter.interpretMessage(msg, hostAddress, String.valueOf(port));
    }
}

