package com.blackhearth.blockchain.block;

public interface BlockMiner {
    Block mineBlock();
    Block mineBlock(String data, String previousHash, long timeStamp, int prefix);
}
