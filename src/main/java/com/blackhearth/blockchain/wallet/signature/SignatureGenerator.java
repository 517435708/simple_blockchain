package com.blackhearth.blockchain.wallet.signature;

import java.security.*;
import java.util.Base64;


public class SignatureGenerator {

    @lombok.SneakyThrows
    public static String applySignature(PrivateKey privateKey, String input) {
        Signature dsa = Signature.getInstance("SHA1withRSA");
        dsa.initSign(privateKey);
        dsa.update(input.getBytes());
        byte[] output = dsa.sign();

        return Base64.getEncoder().encodeToString(output);
    }
}
