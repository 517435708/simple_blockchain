package com.blackhearth.blockchain.validation;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.repository.BlockChainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ChainValidator implements Validator {
    private static final String HASH_STARTS_WITH = "00000";
    private final BlockChainRepository repository;

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
        return new BigDecimal(coins).compareTo(new BigDecimal(params.getTransactionMoneyAmount())) >= 0;
    }

    private boolean isAddressToExists(TransactionParams params) {
        return repository.getPublicKeyFromAddress(params.getAddressTo()).isPresent();
    }

    private boolean isSignValid() {
        throw new UnsupportedOperationException("Uzupełnić przez SignatureGenerator.validateSign() z brancha Błażeja po zmergowaniu");
    }

    private boolean isHashesValid(Block block) {
        List<Block> longest = repository.extractLongestChain();
        if (block.getPreviousHash().isEmpty() && longest.isEmpty()) {
            return true;
        }

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

        return true;
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
