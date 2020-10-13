package com.blackhearth.blockchain.wallet;

public class WalletRunner {

    public static void main(String... args){
        Wallet walletA = new Wallet("test123");
        Wallet walletB = new Wallet("test123");

        System.out.println(walletA.getPrivateKey());
        System.out.println(walletA.getPublicKey());
        System.out.println(walletB.getPrivateKey());
        System.out.println(walletB.getPublicKey());

    }
}
