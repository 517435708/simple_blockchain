package com.blackhearth.blockchain.protocol.message;

import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.WALLET_DATA_RESPONSE;

@Setter
@NoArgsConstructor
public class WalletDataResponseMessage implements Protocol {

    private String amountOfCoins;
    private String publicKey;
    private String address;

    @Override
    public String generateMessage() {
        return WALLET_DATA_RESPONSE.getCode() + amountOfCoins + "|" + publicKey + "|" + address;
    }
}
