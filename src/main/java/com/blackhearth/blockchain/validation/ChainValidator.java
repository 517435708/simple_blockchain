package com.blackhearth.blockchain.validation;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.repository.BlockChainRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ChainValidator implements Validator {
    private static final String HASH_STARTS_WITH = "000001";
    private BlockChainRepository repository;

    @Override
    public boolean isBlockValid(Block block) {
        return isHashesValid(block) && isProofOfWork(block);
    }

    @Override
    public boolean isTransactionValid(TransactionParams params) {
        return isEnoughMoney(params) && isAddressToExists(params) && isSignValid();
    }

    @Override
    public boolean isWalletValid(String hash, String publicKey) {
        return false; //TODO
    }

    private boolean isEnoughMoney(TransactionParams params) {
        String coins = repository.getCoinsFromAddress(params.getAddressFrom()).orElse("0");
        return Integer.parseInt(coins) >= Integer.parseInt(params.getTransactionMoneyAmount());
    }

    private boolean isAddressToExists(TransactionParams params) {
        return repository.getPublicKeyFromAddress(params.getAddressTo()).isPresent();
    }

    private boolean isSignValid() {
        throw new UnsupportedOperationException("Uzupełnić przez SignatureGenerator.validateSign() z brancha Błażeja po zmergowaniu");
    }

    private boolean isHashesValid(Block block) {
        List<Block> blockchain = repository.getChainToBlockHash(block.getPreviousHash());
        if (blockchain.isEmpty()) {
            return false;
        }

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

        return !blockchain.isEmpty();
    }

    private boolean isProofOfWork(Block block) {
        return containsFiveZerosAtStart(block.getHash()) && isHashEqualsWithCalculated(block);
    }

    private boolean containsFiveZerosAtStart(String hash) {
        return hash.startsWith(HASH_STARTS_WITH);
    }

    private boolean isHashEqualsWithCalculated(Block block) {
        return block.getHash().equals(block.calculateBlockHash());
    }
}
