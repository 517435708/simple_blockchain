package com.blackhearth.blockchain.wallet.signature;

import java.security.*;

public class SignatureVerifier {
    public static boolean verifySignature(PublicKey publicKey, String data, String signature) {
        byte[] signatureAsByteArray = signature.getBytes();
        try {
            Signature ecdsaVerify = Signature.getInstance("SHA1withRSA");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signatureAsByteArray);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }

        return false;
    }
}
