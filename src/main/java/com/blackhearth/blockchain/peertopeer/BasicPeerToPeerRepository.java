package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.wallet.WalletData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class BasicPeerToPeerRepository implements PeerToPeerRepository {
    Collection<BlockChainNodeData> knownNodes = Collections.synchronizedCollection(new ArrayList<>());
    Collection<WalletData> knownWallets = Collections.synchronizedCollection(new ArrayList<>());

    @Override
    public List<BlockChainNodeData> getNodes() {
        log.debug("Known hosts are: {}", knownNodes);
        return knownNodes.stream()
                .filter(node -> !node.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public List<WalletData> getWallets() {
        return new ArrayList<>(knownWallets);
    }

    @Override
    public void saveNode(BlockChainNodeData data) {
        boolean nodeExists = knownNodes
                .stream()
                .anyMatch(node -> node.getIp()
                                      .equals(data.getIp()) && node.getPort() == data.getPort());

        if (!nodeExists) {
            log.debug("Saving node: {}:{}",data.getIp(), data.getPort());
            knownNodes.add(data);
        }
    }

    @Override
    public void deleteNode(BlockChainNodeData data) {
        log.debug("Deleting node {}:{}", data.getIp(), data.getPort());
        knownNodes
                .stream()
                .filter(node -> node.getIp().equals(data.getIp()) && node.getPort() == data.getPort())
                .forEach(node -> node.setDeleted(true));
    }

    @Override
    public void saveWalletData(WalletData walletData) {
        knownWallets.add(walletData);
    }

    @Override
    public void saveWalletsAddresses(String[] addresses) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
