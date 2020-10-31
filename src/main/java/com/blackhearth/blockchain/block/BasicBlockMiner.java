package com.blackhearth.blockchain.block;

import org.springframework.stereotype.Component;

@Component
public class BasicBlockMiner implements BlockMiner {

    private Block lastMinedBlock;

    @Override
    public Block lastMinedBlock(String data, String previousHash, long timeStamp, int prefix) {
        Block block = new Block(data, previousHash, timeStamp);
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!block.getHash().substring(0, prefix).equals(prefixString)) {
            block.incrementNonce();
            block.setHash(block.calculateBlockHash());
        }
        lastMinedBlock = block;
        return block;
    }

    @Override
    public Block lastMinedBlock() {
        return lastMinedBlock;
    }
}
