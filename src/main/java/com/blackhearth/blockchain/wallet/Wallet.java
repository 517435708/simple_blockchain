package com.blackhearth.blockchain.wallet;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@Component
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String hash;

    private Transaction lastTransaction;

    public void prepareTransaction(String amount, String address) {
        lastTransaction = new Transaction();
        lastTransaction.setAddress(address);
        lastTransaction.setAmount(amount);
        //TODO lastTransaction.setSign();
    }

}
