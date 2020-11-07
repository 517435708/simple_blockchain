package com.blackhearth.blockchain.wallet;

import com.blackhearth.blockchain.wallet.signature.SignatureUtils;

public class BasicWalletService implements WalletService {
    @Override
    public String getWalletHash(Wallet wallet) {
        return SignatureUtils.applySha256(
                SignatureUtils.getStringFromKey(wallet.getPrivateKey())
                        + SignatureUtils.getStringFromKey(wallet.getPublicKey()));
    }
}
