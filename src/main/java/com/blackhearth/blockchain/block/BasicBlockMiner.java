package com.blackhearth.blockchain.block;

import com.blackhearth.blockchain.block.repository.BlockChainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        log.debug("Started mining.");
        Block builtBlock = blockBuilder.extractBlock();
        builtBlock.setPreviousHash(repository.getLastBlockHash());
        String prefixString = new String(new char[5]).replace('\0', '0');
        builtBlock.setHash(builtBlock.calculateBlockHash());
        while (!builtBlock.getHash()
                          .substring(0, 5)
                          .equals(prefixString)) {
            builtBlock.incrementNonce();
            builtBlock.setHash(builtBlock.calculateBlockHash());
        }
        lastMinedBlock = builtBlock;
        log.debug("Mined: {}", builtBlock);
    }
}
