package com.blackhearth.blockchain.validation;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.wallet.Transaction;
import com.blackhearth.blockchain.wallet.Wallet;

public interface Validator {
    boolean isBlockValid(Block block);
    boolean isTransactionValid(TransactionParams params, Block block);
}
