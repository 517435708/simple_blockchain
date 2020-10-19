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
    private String password;


    public void prepareTransaction(long amount, PublicKey recipientKey) throws TransactionExeption {
        lastTransaction = new Transaction();
        lastTransaction.setSender(this.publicKey);
        lastTransaction.setReciepient(recipientKey);
        lastTransaction.setAddress(this.walletData.getAddress());
        lastTransaction.setAmount(amount);
        lastTransaction.addSignature(this.privateKey);
    }

    // password with SHA1 encryption
    public void setWalletData(String password){
        this.walletData = new WalletData(publicKey, password);
    }

    public void setHash(String password) {
        if (lastTransaction != null) {
            this.setHash(SignatureUtils.applySha256(
                    SignatureUtils.getStringFromKey(publicKey) + password + lastTransaction.getAddress()));
        } else {
            this.setHash(SignatureUtils.applySha256(
                    SignatureUtils.getStringFromKey(publicKey) + password));
        }
    }
}
