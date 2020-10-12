package com.blackhearth.blockchain.block;

public class BlockMiner {

    public Block mineBlock(String data, String previousHash, long timeStamp, int prefix) {
        Block block = new Block(data, previousHash, timeStamp);
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!block.getHash().substring(0, prefix).equals(prefixString)) {
            block.incrementNonce();
            block.setHash(block.calculateBlockHash());
        }
        return block;
    }

    public Block mineBlock() {
        //mine Block without providing data;
        return null;
    }
}
