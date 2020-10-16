package com.blackhearth.blockchain.wallet.transactions;

import com.blackhearth.blockchain.wallet.signature.SignatureGenerator;
import com.blackhearth.blockchain.wallet.signature.SignatureUtils;
import lombok.AccessLevel;
import lombok.Setter;

import java.security.PrivateKey;
import java.security.PublicKey;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Transaction {

    private String address;
    private PublicKey sender; //Senders address/public key.
    private PublicKey reciepient; //Recipients address/public key.
    private Long amount; //Contains the amount we wish to send to the recipient.

    @Setter(AccessLevel.NONE)
    private byte[] signature;

    public Transaction(PublicKey from, PublicKey to, Long amount) {
        this.sender = from;
        this.reciepient = to;
        this.amount = amount;
    }

    public void addSignature(PrivateKey privateKey) {
        if (address != null && sender != null && reciepient != null && amount > 0L) {
            this.signature = SignatureGenerator.applySignature(privateKey, getTransactionData());
        }
    }

    public String getTransactionData() {
        return SignatureUtils.getStringFromKey(sender) +
                SignatureUtils.getStringFromKey(reciepient) +
                address + amount.toString();
    }
}