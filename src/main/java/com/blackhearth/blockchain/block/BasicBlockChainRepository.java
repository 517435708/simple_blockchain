package com.blackhearth.blockchain.block;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.wallet.WalletData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
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
    public List<WalletData> getWalletsFromPosition(int position, int wallets) {
        return null;
    }

    @Override
    public List<BlockChainNodeData> getNodesFromPosition(int position, int increment) {
        return null;
    }
}
