package com.blackhearth.blockchain.wallet;

import com.blackhearth.blockchain.wallet.signature.SignatureUtils;
import lombok.Getter;

import java.security.PublicKey;

@Getter
public class WalletData {
    private String address;

    public WalletData(PublicKey publicKey, String uuid) {
        this.address = SignatureUtils.applySha256(
                SignatureUtils.getStringFromKey(publicKey) + uuid);
    }
}
