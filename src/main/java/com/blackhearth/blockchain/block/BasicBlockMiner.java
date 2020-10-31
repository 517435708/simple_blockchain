package com.blackhearth.blockchain.block;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BasicBlockMiner implements BlockMiner {

    private Block lastMinedBlock;
    private final BlockBuilder blockBuilder;

    @Override
    public Block lastMinedBlock() {
        return lastMinedBlock;
    }

    @Override
    public void startMining() {
        Block builtBlock = blockBuilder.extractBlock();
        String prefixString = new String(new char[5]).replace('\0', '0');
        while (!builtBlock.getHash().substring(0, 5).equals(prefixString)) {
            builtBlock.incrementNonce();
            builtBlock.setHash(builtBlock.calculateBlockHash());
        }
        lastMinedBlock = builtBlock;
    }
}
