package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.wallet.WalletData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BasicPeerToPeerRepository implements PeerToPeerRepository {
    @Override
    public List<BlockChainNodeData> getNodes() {
        return null;
    }

    @Override
    public void saveNode(BlockChainNodeData data) {

    }

    @Override
    public void saveWalletData(WalletData walletData) {

    }

    @Override
    public void saveWalletsAddresses(String[] addresses) {

    }
}
