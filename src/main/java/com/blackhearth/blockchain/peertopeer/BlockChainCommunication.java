package com.blackhearth.blockchain.peertopeer;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface BlockChainCommunication {
    void start(int tcpPort) throws IOException;
    void addListenerOnReceived(Runnable toRunOnReceived);
    void sendTo(String message, String ip, int port) throws IOException;
    void closeConnections();
}
