package com.blackhearth.blockchain.block.repository;


import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.wallet.WalletData;

import java.util.List;
import java.util.Optional;

public interface BlockChainRepository {
    Optional<String> getCoinsFromAddress(String walletAddress);
    List<Block> getLongestChain();
    List<Block> getChainToBlockHash(String hash);
    Optional<String> getPublicKeyFromAddress(String address);
    List<String> getWallets();
    void addToBlockChain(Block block);
}
