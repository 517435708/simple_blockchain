package com.blackhearth.blockchain.validation;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.BlockChainRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ChainValidator implements Validator {
    private BlockChainRepository repository;

    @Override
    public boolean isBlockValid(Block block) {
        return isHashesValid(block) && isProofOfWork();
    }

    @Override
    public boolean isTransactionValid(TransactionParams params, Block block) {
        return isEnoughMoney(params) && isAddressToExists(params) && isSignValid();
    }

    private boolean isEnoughMoney(TransactionParams params) {
        String coins = repository.getCoinsFromAddress(params.getAddressFrom()).orElse("0");
        return Integer.parseInt(coins) >= Integer.parseInt(params.getTransactionMoneyAmount());
    }

    private boolean isAddressToExists(TransactionParams params) {
        return repository.getPublicKeyFromAddress(params.getAddressTo()).isPresent();
    }

    private boolean isSignValid() {
        // TODO: 13.10.2020
        //  SignatureGenerator.validateSign z brancha Błażeja
        return true;
    }

    private boolean isHashesValid(Block block) {
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

        return !blockchain.isEmpty();
    }

    private boolean isProofOfWork() {
        // TODO: 13.10.2020 czy my to w ogóle musimy robić?
        return true;
    }

}
