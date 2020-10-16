package com.blackhearth.blockchain.wallet;
import com.blackhearth.blockchain.wallet.signature.SignatureVerifier;
import com.blackhearth.blockchain.wallet.transactions.Transaction;

public class WalletRunner {

    public static void main(String[] args){
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();

        System.out.println(walletA.getPrivateKey());
        System.out.println(walletA.getPublicKey());
        System.out.println(walletA.getWalletData().getAddress());

        System.out.println(walletB.getPrivateKey());
        System.out.println(walletB.getPublicKey());
        System.out.println(walletB.getWalletData().getAddress());

        Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 200L);
        transaction.setAddress(walletA.getWalletData().getAddress());
        transaction.addSignature(walletA.getPrivateKey());

        System.out.println(transaction.getSignature());
        System.out.println(SignatureVerifier.verifySignature(walletA.getPublicKey(), transaction.getTransactionData() + "_XD" ,transaction.getSignature()));
        System.out.println(SignatureVerifier.verifySignature(walletA.getPublicKey(), transaction.getTransactionData() ,transaction.getSignature()));
        System.out.println(SignatureVerifier.verifySignature(walletB.getPublicKey(), transaction.getTransactionData() + "_XD" ,transaction.getSignature()));
        System.out.println(SignatureVerifier.verifySignature(walletB.getPublicKey(), transaction.getTransactionData() ,transaction.getSignature()));


    }
}
