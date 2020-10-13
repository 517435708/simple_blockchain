package com.blackhearth.blockchain.wallet.signature;


import java.security.*;
import java.util.Arrays;

public class SignatureGenerator {

    public static String applySignature(PrivateKey privateKey, String input) {
        byte[] output = new byte[0];

        try {
            Signature dsa = Signature.getInstance("SHA1withRSA");
            dsa.initSign(privateKey);
            dsa.update(input.getBytes());
            output = dsa.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }

        return Arrays.toString(output);
    }
}
