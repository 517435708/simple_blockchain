package com.blackhearth.blockchain.protocol.message;

import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.NODES_REQUEST;

@Setter
@NoArgsConstructor
class NodesRequestMessage implements Protocol {
    @Override
    public String generateMessage() {
        return NODES_REQUEST.getCode();
    }
}
