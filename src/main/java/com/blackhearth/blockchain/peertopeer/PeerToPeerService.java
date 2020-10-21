package com.blackhearth.blockchain.peertopeer;

import java.io.IOException;

public interface PeerToPeerService {
    void start(int tcpPort) throws IOException;
    void sendMessageTo(String message, String address, String port);
    void sendMessageToAllKnownNodes(String message);
}
