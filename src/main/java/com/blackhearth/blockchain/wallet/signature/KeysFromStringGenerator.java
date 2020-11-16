package com.blackhearth.blockchain.wallet.signature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class KeysFromStringGenerator {

    public static PrivateKey getPrivateKeyFromString(String secertKey) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return generatePrivateKey(secertKey);
    }

    public static PublicKey getPublicKeyFromString(String pubKey) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return generatePublicKey(pubKey);
    }

    private static PrivateKey generatePrivateKey(String privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        StringBuilder pkcs8Lines = getKeyFromString(privateKey);

        PKCS8EncodedKeySpec keySpec = getPkcs8EncodedKeySpec(pkcs8Lines);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    private static PublicKey generatePublicKey(String publicKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        StringBuilder pkcs8Lines = getKeyFromString(publicKey);

        PKCS8EncodedKeySpec keySpec = getPkcs8EncodedKeySpec(pkcs8Lines);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }


    private static PKCS8EncodedKeySpec getPkcs8EncodedKeySpec(StringBuilder pkcs8Lines) {
        String pkcs8Pem = pkcs8Lines.toString();
        pkcs8Pem = pkcs8Pem.replaceAll("\\s+", "");

        byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(pkcs8Pem);

        return new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
    }

    private static StringBuilder getKeyFromString(String privateKey) throws IOException {
        StringBuilder pkcs8Lines = new StringBuilder();
        BufferedReader rdr = new BufferedReader(new StringReader(privateKey));
        String line;

        while ((line = rdr.readLine()) != null) {
            pkcs8Lines.append(line);
        }
        return pkcs8Lines;
    }
}
