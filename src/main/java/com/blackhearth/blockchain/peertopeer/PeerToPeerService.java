package com.blackhearth.blockchain.peertopeer;

public interface PeerToPeerService {
    void sendMessageTo(String message, String address, String port);
    void sendMessageToAllKnownNodes(String message);
}
