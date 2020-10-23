package com.blackhearth.blockchain.wallet.signature;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;

class SignatureApplierTest {

    private static PrivateKey privateKey;

    @BeforeAll
    static void preparePrivateKey() throws Exception {
        privateKey = PrivateKeyProvider.getPrivateKeyFromString();
    }


    @Test
    void applySignaturePassTrue() {
        String appliedSign = SignatureApplier.applySignature(privateKey, "test");
        String expectedSign = "lf1sDNzDJMvftdbOZbXpbvHpnrfHtXksTctuq0R/jIClBKDLS3opCsZ141JJYaYt/f824/2wbxkvQTikKPw94do4lIrPIBZa+zHWcbs1uWko2Wx+A7DEuYmSdM6kASu4XH+qCLHkqTQCSaMNkva5/TAsiL0cilyL4B2eO7FsNdM=";
        Assert.assertEquals(appliedSign, expectedSign);
    }

    @Test
    void applySignaturePassFalse() {
        String appliedSign = SignatureApplier.applySignature(privateKey, "test1");
        String expectedSign = "lf1sDNzDJMvftdbOZbXpbvHpnrfHtXksTctuq0R/jIClBKDLS3opCsZ141JJYaYt/f824/2wbxkvQTikKPw94do4lIrPIBZa+zHWcbs1uWko2Wx+A7DEuYmSdM6kASu4XH+qCLHkqTQCSaMNkva5/TAsiL0cilyL4B2eO7FsNdM=";
        Assert.assertNotEquals(appliedSign, expectedSign);

        appliedSign = SignatureApplier.applySignature(privateKey, "test");
        expectedSign = "XD_lf1sDJMvftdbOZbXpbvHpnrfHtXksTctuq0R/jIClBKDLS3opCsZ141JJYaYt/f824/2wbxkvQTikKPw94do4lIrPIBZa+zHWcbs1uWko2Wx+A7DEuYmSdM6kASu4XH+qCLHkqTQCSaMNkva5/TAsiL0cilyL4B2eO7FsNdM=";
        Assert.assertNotEquals(appliedSign, expectedSign);
    }
}