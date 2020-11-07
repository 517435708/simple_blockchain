package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.wallet.WalletData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class BasicPeerToPeerRepository implements PeerToPeerRepository {
    Collection<BlockChainNodeData> knownNodes = Collections.synchronizedCollection(new ArrayList<>());

    @Override
    public List<BlockChainNodeData> getNodes() {
        log.info("Known hosts are: {}", knownNodes);
        return knownNodes.stream()
                .filter(node -> !node.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public void saveNode(BlockChainNodeData data) {
        boolean nodeExists = knownNodes
                .stream()
                .anyMatch(node -> node.getIp()
                                      .equals(data.getIp()) && node.getPort() == data.getPort());

        if (!nodeExists) {
            log.info("Saving node: {}:{}",data.getIp(), data.getPort());
            knownNodes.add(data);
        }
    }

    @Override
    public void deleteNode(BlockChainNodeData data) {
        log.info("Deleting node {}:{}", data.getIp(), data.getPort());
        knownNodes
                .stream()
                .filter(node -> node.getIp().equals(data.getIp()) && node.getPort() == data.getPort())
                .forEach(node -> node.setDeleted(true));
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
