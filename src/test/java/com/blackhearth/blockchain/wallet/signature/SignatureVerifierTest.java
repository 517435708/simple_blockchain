package com.blackhearth.blockchain.wallet.signature;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;

import static org.junit.jupiter.api.Assertions.*;

class SignatureVerifierTest {

    private static PrivateKey privateKey;

    @BeforeAll
    static void preparePrivateKey() throws Exception {
        privateKey = PrivateKeyProvider.getPrivateKeyFromString();
    }

    @Test
    void verifySignature() {
    }
}