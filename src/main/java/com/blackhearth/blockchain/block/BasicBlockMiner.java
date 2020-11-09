package com.blackhearth.blockchain.block;

import com.blackhearth.blockchain.block.repository.BlockChainRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BasicBlockMiner implements BlockMiner {

    private final BlockBuilder blockBuilder;
    private final BlockChainRepository repository;
    private Block lastMinedBlock;

    @Override
    public Block lastMinedBlock() {
        return lastMinedBlock;
    }

    @Override
    public void startMining() {
        Block builtBlock = blockBuilder.extractBlock();
        String prefixString = new String(new char[5]).replace('\0', '0');
        List<Block> longestChain = repository.extractLongestChain();
        log.info("Longest chain ({}): {}", longestChain.size(), longestChain);
        log.info("Start mining... {}", new Gson().toJson(builtBlock));
        builtBlock.setHash(builtBlock.calculateBlockHash());
        while (!builtBlock.getHash()
                          .substring(0, 5)
                          .equals(prefixString)) {
            builtBlock.incrementNonce();
            builtBlock.setHash(builtBlock.calculateBlockHash());
        }
        log.info("Mined: {}", new Gson().toJson(builtBlock));
        lastMinedBlock = builtBlock;
    }
}
