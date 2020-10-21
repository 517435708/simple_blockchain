package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.wallet.WalletData;

import java.util.List;

public interface PeerToPeerRepository {
    List<BlockChainNodeData> getNodes();
    void saveNode(BlockChainNodeData data);
    void saveWalletData(WalletData walletData);
    void saveWalletsAddresses(String[] addresses);
}
