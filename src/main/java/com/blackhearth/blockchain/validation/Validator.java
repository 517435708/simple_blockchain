package com.blackhearth.blockchain.validation;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.wallet.Transaction;
import com.blackhearth.blockchain.wallet.Wallet;
import org.springframework.stereotype.Component;

@Component
public interface Validator {
    boolean isBlockValid(Block block);
    boolean isTransactionValid(TransactionParams params);
    boolean isWalletValid(String hash, String publicKey);
}
