package com.blackhearth.blockchain.wallet;

import com.blackhearth.blockchain.wallet.signature.SignatureGenerator;
import com.blackhearth.blockchain.wallet.transactions.Transaction;
import lombok.Data;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String hash;

    private Transaction lastTransaction;

    public void prepareTransaction(Long amount, String address) {
        lastTransaction = new Transaction();
        lastTransaction.setAddress(address);
        lastTransaction.setAmount(amount);
        lastTransaction.setSignature(SignatureGenerator.applySignature(privateKey, hash));
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
