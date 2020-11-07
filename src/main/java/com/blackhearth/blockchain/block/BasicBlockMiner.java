package com.blackhearth.blockchain.block;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BasicBlockMiner implements BlockMiner {

    private final BlockBuilder blockBuilder;
    private Block lastMinedBlock;

    @Override
    public Block lastMinedBlock() {
        return lastMinedBlock;
    }

    @Override
    public void startMining() {
        Block builtBlock = blockBuilder.extractBlock();
        String prefixString = new String(new char[5]).replace('\0', '0');
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
