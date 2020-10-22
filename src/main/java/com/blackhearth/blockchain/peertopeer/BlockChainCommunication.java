package com.blackhearth.blockchain.peertopeer;

import java.io.IOException;
import java.util.Set;

public interface BlockChainCommunication {
    void start(int tcpPort) throws IOException;
    void sendTo(String message, String ip, int port) throws IOException;
    Set<HostInfo> getAllKnownHosts();
    void closeConnections();
}
