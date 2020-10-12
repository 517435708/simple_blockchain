package com.blackhearth.blockchain.validation;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.wallet.Transaction;
import com.blackhearth.blockchain.wallet.Wallet;

public interface Validator {
    boolean isTransactionBelong(Transaction transaction, Wallet wallet);
    boolean isProofOfWork(Block block);
    boolean isHashValid(Block block);
    boolean isLongestNode(Block block);
    boolean isTransactionAllowed(Block block, Transaction transaction);
}
