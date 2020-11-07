package com.blackhearth.blockchain.protocol.message;

public interface MessageFactory {
    Protocol generateMessages(ProtocolHeader header, String walletAddress);
    Protocol generateMessages(ProtocolHeader header);
}
