package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.node.BlockChainNodeData;

import java.io.IOException;
import java.util.Set;

public interface BlockChainCommunication {
    void start(int tcpPort) throws IOException;
    void sendTo(String message, String ip, int port) throws IOException;
    Set<BlockChainNodeData> getAllKnownHosts();
    void closeConnections();
}
