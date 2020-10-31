package com.blackhearth.blockchain.wallet.transaction;
import com.blackhearth.blockchain.wallet.signature.SignatureUtils;
import lombok.*;

import java.security.PublicKey;

@Data
@NoArgsConstructor
public class Transaction {

    private String address;
    private PublicKey sender; //Senders address/public key.
    private PublicKey reciepient; //Recipients address/public key.
    private String signature;

    private String amount; //Contains the amount we wish to send to the recipient.
    private long timeStamp;

    public Transaction(String address, PublicKey from, PublicKey to, String amount) {
        this.address = address;
        this.sender = from;
        this.reciepient = to;
        this.amount = amount;
    }

    public String getTransactionData() {
        return SignatureUtils.getStringFromKey(sender) +
                SignatureUtils.getStringFromKey(reciepient) +
                address + "_" +  amount + "_" + timeStamp;
    }
}