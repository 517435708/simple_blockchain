package com.blackhearth.blockchain.wallet.signature;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.*;
import java.util.Base64;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@Slf4j
public class SignatureVerifier {
    public static boolean verifySignature(PublicKey publicKey, String data, String signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("SHA1withRSA");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return ecdsaVerify.verify(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            log.error(e.getMessage());
        }

        return false;
    }
}
