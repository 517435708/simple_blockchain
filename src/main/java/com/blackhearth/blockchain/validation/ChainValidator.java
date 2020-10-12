package com.blackhearth.blockchain.validation;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.BlockChainRepository;
import com.blackhearth.blockchain.wallet.Transaction;

public class ChainValidator implements Validator {
    private BlockChainRepository repository;

    @Override
    public boolean isHashValid(Block block) {
        // TODO: 12.10.2020 zweryfikuj czy aby na pewno to ma sens
        // bo to jest obliczone 2 razy w tym samym obiekcie :(
        String blockHash = block.getHash();
        String calculatedHash = block.calculateBlockHash();

        return blockHash.equals(calculatedHash);
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
