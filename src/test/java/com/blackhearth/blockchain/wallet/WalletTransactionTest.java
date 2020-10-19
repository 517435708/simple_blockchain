package com.blackhearth.blockchain.wallet;

import com.blackhearth.blockchain.wallet.signature.SignatureVerifier;
import com.blackhearth.blockchain.wallet.transactions.Transaction;
import com.blackhearth.blockchain.wallet.transactions.TransactionExeption;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;
import java.security.PublicKey;


class WalletTransactionTest {

    private static Wallet walletA;
    private static Wallet walletB;
    private static Transaction transaction;


    @BeforeAll
    static void initWallets() throws Exception, TransactionExeption {
        walletA = new Wallet();
        walletB = new Wallet();

        KeysGenerator keysGeneratorA = new KeysGenerator(512);
        PrivateKey skA = keysGeneratorA.getPrivateKey();
        PublicKey pkA = keysGeneratorA.getPublicKey();

        KeysGenerator keysGeneratorB = new KeysGenerator(512);
        PrivateKey skB = keysGeneratorB.getPrivateKey();
        PublicKey pkB = keysGeneratorB.getPublicKey();

        walletA.setPrivateKey(skA);
        walletA.setPublicKey(pkA);

        walletB.setPrivateKey(skB);
        walletB.setPublicKey(pkB);

        walletA.setWalletData("test123");
        walletB.setWalletData("321tset");

        walletA.prepareTransaction(100L, walletB.getPublicKey());
    }

    @BeforeAll
    static void initTransaction() throws TransactionExeption {
        transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 200L);
        transaction.setAddress(walletA.getWalletData().getAddress());
        transaction.addSignature(walletA.getPrivateKey());
    }


    @Test
    void verifySignatureTestTrue() {
        Assert.assertTrue(SignatureVerifier
                .verifySignature(walletA.getPublicKey(), transaction.getTransactionData(), transaction.getSignature()));
    }

    @Test
    void verifySignatureTestFalseForPublicKeyWalletA() {
        Assert.assertFalse(SignatureVerifier
                .verifySignature(walletA.getPublicKey(), transaction.getTransactionData() + "_test", transaction.getSignature()));
    }

    @Test
    void verifySignatureTestFalseForPublicKeyWalletB() {
        Assert.assertFalse(SignatureVerifier
                .verifySignature(walletB.getPublicKey(), transaction.getTransactionData() + "_test123", transaction.getSignature()));
        Assert.assertFalse(SignatureVerifier
                .verifySignature(walletB.getPublicKey(), transaction.getTransactionData(), transaction.getSignature()));
    }
}