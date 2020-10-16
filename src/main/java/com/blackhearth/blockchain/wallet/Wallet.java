package com.blackhearth.blockchain.wallet;

import com.blackhearth.blockchain.wallet.signature.SignatureGenerator;
import com.blackhearth.blockchain.wallet.signature.SignatureUtils;
import com.blackhearth.blockchain.wallet.transactions.Transaction;
import lombok.Data;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

@Data
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private WalletData walletData;
    private Transaction lastTransaction;
    private String hash;


    public void prepareTransaction(Long amount, String address, PublicKey recipientKey) {
        lastTransaction = new Transaction();
        lastTransaction.setSender(publicKey);
        lastTransaction.setReciepient(recipientKey);
        lastTransaction.setAddress(address);
        lastTransaction.setAmount(amount);
        lastTransaction.addSignature(privateKey);
    }

    public Wallet() {
        generateKeyPair();
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        this.walletData = new WalletData(publicKey, uuid);
        if(lastTransaction != null) {
            this.setHash(SignatureUtils.applySha256(
                    SignatureUtils.getStringFromKey(publicKey) + uuid + lastTransaction.getAddress()));
        } else {
            this.setHash(SignatureUtils.applySha256(
                    SignatureUtils.getStringFromKey(publicKey) + uuid));
        }

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
