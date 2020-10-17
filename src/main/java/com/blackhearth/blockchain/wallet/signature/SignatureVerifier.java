package com.blackhearth.blockchain.wallet.signature;

import java.security.*;
import java.util.Base64;

public class SignatureVerifier {
    public static boolean verifySignature(PublicKey publicKey, String data, String signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("SHA1withRSA");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return ecdsaVerify.verify(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }

        return false;
    }
}
