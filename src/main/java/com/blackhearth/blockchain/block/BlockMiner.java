package com.blackhearth.blockchain.block;


public interface BlockMiner {
    Block lastMinedBlock();
    void startMining();
}
