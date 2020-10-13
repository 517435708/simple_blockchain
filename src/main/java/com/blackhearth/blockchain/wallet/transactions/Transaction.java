package com.blackhearth.blockchain.wallet.transactions;

import com.blackhearth.blockchain.wallet.signature.SignatureUtils;
import lombok.Data;

import java.security.PublicKey;

@Data
public class Transaction {

    private String address;
    private PublicKey sender; //Senders address/public key.
    private PublicKey reciepient; //Recipients address/public key.
    private Long amount; //Contains the amount we wish to send to the recipient.
    private String signature;

    public Transaction(PublicKey from, PublicKey to, Long amount) {
        this.sender = from;
        this.reciepient = to;
        this.amount = amount;
    }

    private String calulateHash() {
        return SignatureUtils.applySha256(
                SignatureUtils.getStringFromKey(sender) +
                        SignatureUtils.getStringFromKey(reciepient) +
                        this.amount);
    }
}