package com.blackhearth.blockchain.protocol.message;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.CHAIN_REQUEST;

public class ChainRequestMessage implements Protocol{
    @Override
    public String generateMessage() {
        return CHAIN_REQUEST.getCode();
    }
}
