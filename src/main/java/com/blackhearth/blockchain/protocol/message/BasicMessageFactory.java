package com.blackhearth.blockchain.protocol.message;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.BlockMiner;
import com.blackhearth.blockchain.block.repository.BlockChainRepository;
import com.blackhearth.blockchain.node.BlockChainNode;
import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.peertopeer.PeerToPeerRepository;
import com.blackhearth.blockchain.wallet.Wallet;
import com.blackhearth.blockchain.wallet.WalletData;
import com.blackhearth.blockchain.wallet.transaction.Transaction;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class BasicMessageFactory implements MessageFactory {

    private final BlockMiner blockMiner;
    private final BlockChainNode blockChainNode;
    private final BlockChainRepository blockChainRepository;
    private final PeerToPeerRepository peerToPeerRepository;
    private final Wallet wallet;

    public BasicMessageFactory(BlockMiner blockMiner,
                               @Lazy BlockChainNode blockChainNode,
                               BlockChainRepository blockChainRepository,
                               PeerToPeerRepository peerToPeerRepository, Wallet wallet) {
        this.blockMiner = blockMiner;
        this.blockChainNode = blockChainNode;
        this.blockChainRepository = blockChainRepository;
        this.peerToPeerRepository = peerToPeerRepository;
        this.wallet = wallet;
    }

    @Override
    public Protocol generateMessages(ProtocolHeader header, String walletAddress) {
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
            case CHAIN_REQUEST:
                return chainRequest();
            case CHAIN_RESPONSE:
                return chainResponse();
            default:
                return () -> "DUMMY";
        }
    }

    @Override
    public Protocol generateMessages(ProtocolHeader header) {
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
            case CHAIN_REQUEST:
                return chainRequest();
            case CHAIN_RESPONSE:
                return chainResponse();
            default:
                return () -> "DUMMY";
        }
    }

    private Protocol chainResponse() {
        ChainResponseMessage chainResponse = new ChainResponseMessage();
        chainResponse.setChain(blockChainRepository.extractLongestChain());
        return chainResponse;
    }

    private Protocol chainRequest() {
        return new ChainRequestMessage();
    }

    private Protocol generateNodesResponseMessage() {
        List<BlockChainNodeData> data = peerToPeerRepository.getNodes();
        NodesResponseMessage nodesResponseMessage = new NodesResponseMessage();
        nodesResponseMessage.setNodes(new ArrayList<>(data));
        return nodesResponseMessage;
    }

    private Protocol generateNodesRequestMessage() {
        return new NodesRequestMessage();
    }

    private Protocol generateWalletsResponseMessage() {
        List<String> data = peerToPeerRepository.getWalletsData()
                .stream()
                .map(WalletData::getAddress)
                .collect(Collectors.toList());
        WalletsResponseMessage walletsResponseMessage = new WalletsResponseMessage();
        walletsResponseMessage.setWallets(data);
        return walletsResponseMessage;
    }

    private Protocol generateWalletsRequestMessage() {
        return new WalletsRequestMessage();
    }

    private Protocol generateWalletDataResponseMessage(String address) {
        var walletData = peerToPeerRepository
                .getWalletsData()
                .stream()
                .filter(el -> el.getAddress().equals(address))
                .findFirst()
                .orElseThrow();

        WalletDataResponseMessage walletDataResponseMessage = new WalletDataResponseMessage();
        walletDataResponseMessage.setAddress(walletData.getAddress());
        walletDataResponseMessage.setAmountOfCoins(walletData.getAmountOfMoney());
        walletDataResponseMessage.setPublicKey(walletData.getPublicKey());
        return walletDataResponseMessage;
    }

    private Protocol generateWalletDataRequestMessage() {
        WalletDataRequestMessage walletDataRequestMessage = new WalletDataRequestMessage();
        walletDataRequestMessage.setWalletAddress(wallet.getHash());
        return walletDataRequestMessage;
    }

    private Protocol generateAddTransactionMessage() {
        TransactionMessage transactionMessage = new TransactionMessage();
        transactionMessage.setSenderAddress(wallet.getHash());

        Transaction transaction = wallet.getLastTransaction();
        transactionMessage.setAmountOfCoinTransferred(transaction.getAmount());
        transactionMessage.setReceiverAddress(transaction.getAddress());
        transactionMessage.setDigitalSignature(transaction.getSignature());
        transactionMessage.setTimeStamp(transaction.getTimeStamp());
        return transactionMessage;
    }

    private Protocol generateAddBlockMessage() {
        Block block = blockMiner.lastMinedBlock();
        AddBlockMessage addBlockMessage = new AddBlockMessage();
        addBlockMessage.setBlock(block);
        return addBlockMessage;
    }

    private Protocol generateNotifyWalletMessage() {
        NotifyWalletMessage notifyWalletMessage = new NotifyWalletMessage();
        notifyWalletMessage.setPublicKey(wallet.getPublicKey()
                .toString());
        notifyWalletMessage.setWalletHash(wallet.getHash());
        return notifyWalletMessage;
    }

    @SneakyThrows
    private Protocol generateNotifyNodeMessage() {
        NotifyNodeMessage notifyNodeMessage = new NotifyNodeMessage();
        BlockChainNodeData data = blockChainNode.composeData();

        notifyNodeMessage.setBlockChainNode(data);
        return notifyNodeMessage;
    }
}
