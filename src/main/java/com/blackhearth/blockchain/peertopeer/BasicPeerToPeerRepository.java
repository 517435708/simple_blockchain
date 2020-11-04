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
    public void saveWalletData(WalletData walletData) {

    }

    @Override
    public void saveWalletsAddresses(String[] addresses) {

    }
}
