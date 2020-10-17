package com.blackhearth.blockchain.block;


import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.wallet.Wallet;
import com.blackhearth.blockchain.wallet.WalletData;

import java.util.List;
import java.util.Optional;

public interface BlockChainRepository {
    Optional<String> getCoinsFromAddress(String walletAddress);
    List<Block> getBlocksFromPosition(int position, int blocks);
    Optional<Integer> getPositionFromBlockHash(String hash);
    Optional<String> getPublicKeyFromAddress(String address);
    List<WalletData> getWalletsFromPosition(int position, int wallets);

    List<BlockChainNodeData> getNodesFromPosition(int position, int increment);
    void addToBlockChain(Block block);
}
