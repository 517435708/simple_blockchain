package com.blackhearth.blockchain.wallet;

import com.blackhearth.blockchain.wallet.transaction.Transaction;
import lombok.Data;

import java.security.PrivateKey;
import java.security.PublicKey;

@Data
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String hash = "";
    private Transaction lastTransaction;
}
