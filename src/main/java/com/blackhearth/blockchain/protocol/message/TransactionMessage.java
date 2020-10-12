package com.blackhearth.blockchain.protocol.message;

import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.TRANSACTION;

@Setter
@NoArgsConstructor
class TransactionMessage implements Protocol {

    private String senderAddress;
    private String receiverAddress;
    private String amountOfCoinTransferred;
    private String digitalSignature;

    @Override
    public String generateMessage() {
        return TRANSACTION.getCode() + senderAddress + "|" + receiverAddress + "|" + amountOfCoinTransferred + "|" + digitalSignature;
    }
}
