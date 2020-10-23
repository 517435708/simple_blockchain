package com.blackhearth.blockchain.protocol.interpreter;

public interface ProtocolInterpreter {
    void interpretMessage(String message, String senderAddress, String senderPort);
}
