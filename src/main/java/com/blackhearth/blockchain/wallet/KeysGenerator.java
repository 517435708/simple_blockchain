package com.blackhearth.blockchain.wallet;

import lombok.Getter;

import java.security.*;

@Getter
public class KeysGenerator {

    private KeyPairGenerator keyGen;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public KeysGenerator(int keylength) throws NoSuchAlgorithmException {
        keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keylength);
        createKeys();
    }

    private void createKeys() {
        KeyPair pair = keyGen.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
    }
}