package com.blackhearth.blockchain.wallet;

import com.blackhearth.blockchain.wallet.signature.SignatureUtils;
import lombok.Data;

@Data
public class SimpleWallet {
    private String privateKey;
    private String publicKey;
    private String walletAddress;

    public SimpleWallet(Wallet wallet) {
        privateKey = SignatureUtils.getStringFromKey(wallet.getPrivateKey());
        publicKey = SignatureUtils.getStringFromKey(wallet.getPublicKey());
        walletAddress = wallet.getHash();
    }
}
