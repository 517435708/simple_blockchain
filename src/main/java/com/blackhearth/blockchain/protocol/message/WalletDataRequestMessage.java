package com.blackhearth.blockchain.protocol.message;

import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.WALLET_DATA_REQUEST;

@Setter
@NoArgsConstructor
class WalletDataRequestMessage implements Protocol {

    private String walletAddress;

    @Override
    public String generateMessage() {
        return WALLET_DATA_REQUEST.getCode() + walletAddress;
    }
}
