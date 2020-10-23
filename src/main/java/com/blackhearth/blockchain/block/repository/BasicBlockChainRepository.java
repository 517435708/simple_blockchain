package com.blackhearth.blockchain.block.repository;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.wallet.WalletData;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BasicBlockChainRepository implements BlockChainRepository {
    @Override
    public Optional<String> getCoinsFromAddress(String walletAddress) {
        return Optional.empty();
    }

    @Override
    public List<Block> getBlocksFromPosition(int position, int blocks) {
        return null;
    }

    @Override
    public Optional<Integer> getPositionFromBlockHash(String hash) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getPublicKeyFromAddress(String address) {
        return Optional.empty();
    }

    @Override
    public List<WalletData> getWallets() {
        return null;
    }

    @Override
    public List<BlockChainNodeData> getNodes() {
        return null;
    }

    @Override
    public void addToBlockChain(Block block) {

    }
}
