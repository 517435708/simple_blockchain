package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.wallet.WalletData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
@Slf4j
public class BasicPeerToPeerRepository implements PeerToPeerRepository {
    private Set<BlockChainNodeData> knownNodes = new HashSet<>();

    @Override
    public Set<BlockChainNodeData> getNodes() {
        log.info("Known hosts are: {}", knownNodes);
        return knownNodes;
    }

    @Override
    public void saveNode(BlockChainNodeData data) {
        log.info("Saving node: {}:{}",data.getIp(), data.getPort());
        knownNodes.add(data);
    }

    @Override
    public void deleteNode(BlockChainNodeData data) {
        log.info("Deleting node {}:{}", data.getIp(), data.getPort());
        knownNodes.remove(data);
    }

    @Override
    public void saveWalletData(WalletData walletData) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void saveWalletsAddresses(String[] addresses) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
