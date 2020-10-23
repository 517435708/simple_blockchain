package com.blackhearth.blockchain.wallet.transaction;

import java.security.PrivateKey;

interface TransactionService {
    String addSignature(PrivateKey privateKey, Transaction transaction);
}
