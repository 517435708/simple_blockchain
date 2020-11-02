package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.wallet.WalletData;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class BasicPeerToPeerRepository implements PeerToPeerRepository {
    private Set<BlockChainNodeData> knownNodes = new HashSet<>();

    @Override
    public Set<BlockChainNodeData> getNodes() {
        return knownNodes;
    }

    @Override
    public void saveNode(BlockChainNodeData data) {
        knownNodes.add(data);
    }

    @Override
    public void saveWalletData(WalletData walletData) {

    }

    @Override
    public void saveWalletsAddresses(String[] addresses) {

    }
}
