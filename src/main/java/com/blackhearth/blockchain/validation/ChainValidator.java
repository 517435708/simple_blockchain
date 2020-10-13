package com.blackhearth.blockchain.validation;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.BlockChainRepository;
import com.blackhearth.blockchain.wallet.Transaction;
import com.blackhearth.blockchain.wallet.Wallet;

import java.util.List;

public class ChainValidator implements Validator {
    private BlockChainRepository repository;

    @Override
    public boolean isTransactionBelong(Transaction transaction, Wallet wallet) {
        return false;
    }

    @Override
    public boolean isProofOfWork(Block block) {
        return false;
    }

    @Override
    public boolean isHashValid(Block block) {
        int blockPosition = repository.getPositionFromBlockHash(block.getHash()).orElse(0);
        List<Block> blockchain = repository.getBlocksFromPosition(0, blockPosition);

        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateBlockHash())) {
                return false;
            }

            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isLongestNode(Block block) {

        return false;
    }

    @Override
    public boolean isTransactionAllowed(Block block, Transaction transaction) {
        // TODO: 12.10.2020 to samo co wyżej. Kto to wie czy to jest okej :|
        String coinsFromAddress = repository.getCoinsFromAddress(transaction.getAddress()).orElse("0");

        int accountCoins = Integer.parseInt(coinsFromAddress);
        int transactionAmount = Integer.parseInt(transaction.getAmount());

        return (accountCoins - transactionAmount) > 0;
    }
}
