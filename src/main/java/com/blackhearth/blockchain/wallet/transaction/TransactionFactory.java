package com.blackhearth.blockchain.wallet.transaction;

import java.security.PrivateKey;

interface TransactionFactory {
    String addSignature(PrivateKey privateKey, Transaction transaction);
}
