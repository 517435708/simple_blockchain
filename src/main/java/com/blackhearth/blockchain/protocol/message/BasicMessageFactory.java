package com.blackhearth.blockchain.protocol.message;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.BlockChainRepository;
import com.blackhearth.blockchain.block.BlockMiner;
import com.blackhearth.blockchain.node.BlockChainNode;
import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.node.BlockChainNodeException;
import com.blackhearth.blockchain.wallet.Transaction;
import com.blackhearth.blockchain.wallet.Wallet;
import com.blackhearth.blockchain.wallet.WalletData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class BasicMessageFactory implements MessageFactory {

    private final BlockMiner blockMiner;
    private final BlockChainNode blockChainNode;
    private final BlockChainRepository blockChainRepository;

    // TODO: 18.10.2020 do sth
    private final Wallet wallet = new Wallet();

    @Override
    public List<Protocol> generateMessages(ProtocolHeader header, String walletAddress) throws
                                                                                        BlockChainNodeException {
        switch (header) {
            case NOTIFY_NODE:
                return generateNotifyNodeMessage();
            case NOTIFY_WALLET:
                return generateNotifyWalletMessage();
            case ADD_BLOCK:
                return generateAddBlockMessage();
            case TRANSACTION:
                return generateAddTransactionMessage();
            case WALLET_DATA_REQUEST:
                return generateWalletDataRequestMessage();
            case WALLET_DATA_RESPONSE:
                return generateWalletDataResponseMessage(walletAddress);
            case WALLETS_REQUEST:
                return generateWalletsRequestMessage();
            case WALLETS_RESPONSE:
                return generateWalletsResponseMessage();
            case NODES_REQUEST:
                return generateNodesRequestMessage();
            case NODES_RESPONSE:
                return generateNodesResponseMessage();
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public List<Protocol> generateMessages(ProtocolHeader header) throws
                                                                  BlockChainNodeException {
        switch (header) {
            case NOTIFY_NODE:
                return generateNotifyNodeMessage();
            case NOTIFY_WALLET:
                return generateNotifyWalletMessage();
            case ADD_BLOCK:
                return generateAddBlockMessage();
            case TRANSACTION:
                return generateAddTransactionMessage();
            case WALLET_DATA_REQUEST:
                return generateWalletDataRequestMessage();
            case WALLETS_REQUEST:
                return generateWalletsRequestMessage();
            case WALLETS_RESPONSE:
                return generateWalletsResponseMessage();
            case NODES_REQUEST:
                return generateNodesRequestMessage();
            case NODES_RESPONSE:
                return generateNodesResponseMessage();
            default:
                return Collections.emptyList();
        }
    }

    private List<Protocol> generateNodesResponseMessage() {
        int position = 0;
        int increment = 10;
        List<BlockChainNodeData> data;
        List<Protocol> protocols = new ArrayList<>();
        do {
            data = blockChainRepository.getNodesFromPosition(position, increment);
            NodesResponseMessage nodesResponseMessage = new NodesResponseMessage();
            nodesResponseMessage.setNodes(data);
            nodesResponseMessage.setMessagePosition(String.valueOf(position));
            protocols.add(nodesResponseMessage);
            position += increment;
        } while (!data.isEmpty());
        return protocols;
    }

    private List<Protocol> generateNodesRequestMessage() {
        return Collections.singletonList(new NodesRequestMessage());
    }

    private List<Protocol> generateWalletsResponseMessage() {
        int position = 0;
        int increment = 10;
        List<WalletData> data;
        List<Protocol> protocols = new ArrayList<>();
        do {
            data = blockChainRepository.getWalletsFromPosition(position, increment);
            WalletsResponseMessage walletsResponseMessage = new WalletsResponseMessage();
            walletsResponseMessage.setWallets(data.stream()
                                                  .map(WalletData::getAddress)
                                                  .collect(Collectors.toList()));
            walletsResponseMessage.setMessagePosition(String.valueOf(position));
            protocols.add(walletsResponseMessage);
            position += increment;
        } while (!data.isEmpty());
        return protocols;
    }

    private List<Protocol> generateWalletsRequestMessage() {
        return Collections.singletonList(new WalletsRequestMessage());
    }

    private List<Protocol> generateWalletDataResponseMessage(String address) {
        WalletDataResponseMessage walletDataResponseMessage = new WalletDataResponseMessage();
        walletDataResponseMessage.setAddress(address);
        walletDataResponseMessage.setAmountOfCoins(blockChainRepository.getCoinsFromAddress(address)
                                                                       .orElse(""));
        walletDataResponseMessage.setPublicKey(blockChainRepository.getPublicKeyFromAddress(address)
                                                                   .orElse(""));
        return Collections.singletonList(walletDataResponseMessage);
    }

    private List<Protocol> generateWalletDataRequestMessage() {
        WalletDataRequestMessage walletDataRequestMessage = new WalletDataRequestMessage();
        walletDataRequestMessage.setWalletAddress(wallet.getHash());
        return Collections.singletonList(walletDataRequestMessage);
    }

    private List<Protocol> generateAddTransactionMessage() {
        TransactionMessage transactionMessage = new TransactionMessage();
        transactionMessage.setSenderAddress(wallet.getHash());
        Transaction transaction = wallet.getLastTransaction();
        transactionMessage.setAmountOfCoinTransferred(transaction.getAmount());
        transactionMessage.setReceiverAddress(transaction.getAddress());
        transactionMessage.setDigitalSignature(transaction.getSign());
        return Collections.singletonList(transactionMessage);
    }

    private List<Protocol> generateAddBlockMessage() {
        Block block = blockMiner.mineBlock();
        AddBlockMessage addBlockMessage = new AddBlockMessage();
        addBlockMessage.setBlock(block);
        return Collections.singletonList(addBlockMessage);
    }

    private List<Protocol> generateNotifyWalletMessage() {
        NotifyWalletMessage notifyWalletMessage = new NotifyWalletMessage();
        notifyWalletMessage.setPublicKey(wallet.getPublicKey()
                                               .toString());
        notifyWalletMessage.setWalletHash(wallet.getHash());
        return Collections.singletonList(notifyWalletMessage);
    }

    private List<Protocol> generateNotifyNodeMessage() throws
                                                       BlockChainNodeException {
        NotifyNodeMessage notifyNodeMessage = new NotifyNodeMessage();
        BlockChainNodeData data = blockChainNode.start();
        notifyNodeMessage.setBlockChainNode(data);
        return Collections.singletonList(notifyNodeMessage);
    }
}
