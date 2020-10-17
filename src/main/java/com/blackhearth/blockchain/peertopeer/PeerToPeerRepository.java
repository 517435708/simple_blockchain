package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.node.BlockChainNodeData;

import java.util.List;

public interface PeerToPeerRepository {
    List<BlockChainNodeData> getNodes();
    void saveNode(BlockChainNodeData data);
}
