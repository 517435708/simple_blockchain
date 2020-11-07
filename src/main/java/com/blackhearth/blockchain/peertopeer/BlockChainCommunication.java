package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.node.BlockChainNodeData;

import java.io.IOException;
import java.util.List;

public interface BlockChainCommunication {
    void start(int tcpPort) throws IOException;
    void sendTo(String message, String ip, int port) throws IOException;
    List<BlockChainNodeData> getAllKnownHosts();
    void closeConnections();
}
