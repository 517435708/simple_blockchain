package com.blackhearth.blockchain.wallet;

import com.blackhearth.blockchain.wallet.signature.SignatureUtils;
import com.blackhearth.blockchain.wallet.transactions.Transaction;
import com.blackhearth.blockchain.wallet.transactions.TransactionExeption;
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


    public void prepareTransaction(long amount, PublicKey recipientKey) throws TransactionExeption {
        lastTransaction = new Transaction();
        lastTransaction.setSender(this.publicKey);
        lastTransaction.setReciepient(recipientKey);
        lastTransaction.setAddress(this.walletData.getAddress());
        lastTransaction.setAmount(amount);
        lastTransaction.addSignature(this.privateKey);
    }

    public Wallet() {
        generateKeyPair();
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        this.walletData = new WalletData(publicKey, uuid);
        hashSetter(uuid);
    }

    private void hashSetter(String uuid) {
        if (lastTransaction != null) {
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
