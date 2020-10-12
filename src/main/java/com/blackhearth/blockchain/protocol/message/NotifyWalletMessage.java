package com.blackhearth.blockchain.protocol.message;

import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.NOTIFY_WALLET;

@Setter
@NoArgsConstructor
class NotifyWalletMessage implements Protocol{

    private String publicKey;
    private String walletHash;

    @Override
    public String generateMessage() {
        return NOTIFY_WALLET.getCode() + publicKey + "HASH:" + walletHash;
    }
}
