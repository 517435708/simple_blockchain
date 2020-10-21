package com.blackhearth.blockchain.protocol.interpreter;

import java.util.Optional;

public interface ProtocolInterpreter {
    void interpretMessage(String message, String senderAddress, String senderPort);
}
