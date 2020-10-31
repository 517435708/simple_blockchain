package com.blackhearth.blockchain.block;


public interface BlockMiner {
    Block lastMinedBlock();
    Block lastMinedBlock(String data, String previousHash, long timeStamp, int prefix);
}
