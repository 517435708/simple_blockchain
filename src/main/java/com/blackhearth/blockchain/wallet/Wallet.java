package com.blackhearth.blockchain.wallet;

import lombok.Data;

import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
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

    public Wallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {

        try {
            KeysGenerator keysGenerator = new KeysGenerator(512);
            privateKey = keysGenerator.getPrivateKey();
            publicKey = keysGenerator.getPublicKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

}
