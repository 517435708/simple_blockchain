package com.blackhearth.blockchain.wallet;

import com.blackhearth.blockchain.wallet.signature.SignatureVerifier;
import com.blackhearth.blockchain.wallet.transaction.Transaction;
import com.blackhearth.blockchain.wallet.transaction.BasicTransactionService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;
import java.security.PublicKey;


class WalletTransactionBuilderTest {

    private static Wallet walletA;
    private static Wallet walletB;
    private static Transaction transaction;
    private static BasicTransactionService basicTransactionFactory;
    private static BasicWalletService basicWalletFactory;


    @BeforeAll
    static void initWallets() throws Exception {
        walletA = new Wallet();
        walletB = new Wallet();

        basicWalletFactory = new BasicWalletService();

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


        walletA.setHash(basicWalletFactory.setWalletHash(walletA));
        walletB.setHash(basicWalletFactory.setWalletHash(walletB));


        Transaction lastTransaction = new Transaction();
        lastTransaction.setAddress(walletA.getHash());
        lastTransaction.setAmount("100");
        lastTransaction.setReciepient(walletA.getPublicKey());
        lastTransaction.setSender(walletB.getPublicKey());
        lastTransaction.setTimeStamp(1235);
        lastTransaction.setSignature("sign");

        walletA.setLastTransaction(lastTransaction);
    }

    @BeforeAll
    static void initTransaction() {
        basicTransactionFactory = new BasicTransactionService();

        transaction = new Transaction(walletA.getHash(), walletA.getPublicKey(), walletB.getPublicKey(), "200");
        transaction.setSignature(basicTransactionFactory.addSignature(walletA.getPrivateKey(), transaction));
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