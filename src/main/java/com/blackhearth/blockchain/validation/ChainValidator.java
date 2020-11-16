package com.blackhearth.blockchain.validation;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.repository.BlockChainRepository;
import com.blackhearth.blockchain.wallet.signature.KeysFromStringGenerator;
import com.blackhearth.blockchain.wallet.signature.SignatureVerifier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
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
        return isEnoughMoney(params) && isAddressToExists(params) && isSignValid(params);
    }

    @Override
    public boolean isWalletValid(String hash, String publicKey) {
        return true; //TODO
    }

    private boolean isEnoughMoney(TransactionParams params) {
        String coins = repository.getCoinsFromAddress(params.getAddressFrom()).orElse("0");
        return new BigDecimal(coins).compareTo(new BigDecimal(params.getTransactionMoneyAmount())) >= 0;
    }

    private boolean isAddressToExists(TransactionParams params) {
        return repository.getPublicKeyFromAddress(params.getAddressTo()).isPresent();
    }


    @SneakyThrows({InvalidKeySpecException.class, NoSuchAlgorithmException.class, IOException.class})
    private boolean isSignValid(TransactionParams params) {
        String data = params.getAddressFrom() + "|" + params.getAddressTo() + "|" +
                params.getTransactionMoneyAmount() + "|" + params.getTimeStamp();
        String signature = params.getDigitalSign();

        String publicKey = String.valueOf(repository.getPublicKeyFromAddress(params.getAddressFrom()));
        PublicKey pubKey = KeysFromStringGenerator.getPublicKeyFromString(publicKey);
        return SignatureVerifier.verifySignature(pubKey, data, signature);
    }

    private boolean isHashesValid(Block block) {
        if (block.getPreviousHash().isEmpty()) {
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
