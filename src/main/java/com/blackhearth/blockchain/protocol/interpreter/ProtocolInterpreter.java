package com.blackhearth.blockchain.protocol.interpreter;

import java.util.Optional;

public interface ProtocolInterpreter {
    Optional<String> interpretMessage(String message);
}
