package com.blackhearth.blockchain.protocol.message;

import lombok.NoArgsConstructor;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.WALLETS_REQUEST;


@NoArgsConstructor
class WalletsRequestMessage implements Protocol {

    @Override
    public String generateMessage() {
        return WALLETS_REQUEST.getCode();
    }
}
