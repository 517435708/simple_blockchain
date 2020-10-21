package com.blackhearth.blockchain.peertopeer;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BasicPeerToPeerService implements PeerToPeerService {
    private BlockChainCommunication communication;

    @Autowired
    public BasicPeerToPeerService(BlockChainCommunication communication) {
        this.communication = communication;
    }

    @Override
    public void start(int tcpPort) throws IOException {
        communication.start(tcpPort);
        communication.addListenerOnReceived(() -> System.out.println("SUCCESS! RECEIVED STH"));
    }

    @SneakyThrows
    @Override
    public void sendMessageTo(String message, String address, String port) {
        communication.sendTo(message, address, Integer.parseInt(port));
    }

    @Override
    public void sendMessageToAllKnownNodes(String message) {
        // TODO: 21.10.2020
    }
}
