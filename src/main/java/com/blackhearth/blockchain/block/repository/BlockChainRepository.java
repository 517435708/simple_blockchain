package com.blackhearth.blockchain.block.repository;


import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.wallet.WalletData;

import java.util.List;
import java.util.Optional;

public interface BlockChainRepository {
    Optional<String> getCoinsFromAddress(String walletAddress);
    List<Block> getBlocksFromPosition(int position, int blocks);
    Optional<Integer> getPositionFromBlockHash(String hash);
    Optional<String> getPublicKeyFromAddress(String address);
    List<WalletData> getWallets();

    List<BlockChainNodeData> getNodes();
    void addToBlockChain(Block block);
}
