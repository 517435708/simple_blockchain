package com.blackhearth.blockchain.protocol.message;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.repository.BlockChainRepository;
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

import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class BasicMessageFactory implements MessageFactory {

    private final BlockMiner blockMiner;
    private final BlockChainNode blockChainNode;
    private final BlockChainRepository blockChainRepository;
    private final Wallet wallet;

    @Override
    public Protocol generateMessages(ProtocolHeader header, String walletAddress) throws
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
                throw new BlockChainNodeException("Wrong Key");
        }
    }

    @Override
    public Protocol generateMessages(ProtocolHeader header) throws
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
                throw new BlockChainNodeException("Wrong Key");
        }
    }

    private Protocol generateNodesResponseMessage() {
        List<BlockChainNodeData> data = blockChainRepository.getNodes();
        NodesResponseMessage nodesResponseMessage = new NodesResponseMessage();
        nodesResponseMessage.setNodes(data);
        return nodesResponseMessage;
    }

    private Protocol generateNodesRequestMessage() {
        return new NodesRequestMessage();
    }

    private Protocol generateWalletsResponseMessage() {
        List<WalletData> data = blockChainRepository.getWallets();
        WalletsResponseMessage walletsResponseMessage = new WalletsResponseMessage();
        walletsResponseMessage.setWallets(data.stream()
                                              .map(WalletData::getAddress)
                                              .collect(Collectors.toList()));
        return walletsResponseMessage;
    }

    private Protocol generateWalletsRequestMessage() {
        return new WalletsRequestMessage();
    }

    private Protocol generateWalletDataResponseMessage(String address) {
        WalletDataResponseMessage walletDataResponseMessage = new WalletDataResponseMessage();
        walletDataResponseMessage.setAddress(address);
        walletDataResponseMessage.setAmountOfCoins(blockChainRepository.getCoinsFromAddress(address)
                                                                       .orElse(""));
        walletDataResponseMessage.setPublicKey(blockChainRepository.getPublicKeyFromAddress(address)
                                                                   .orElse(""));
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
        transactionMessage.setDigitalSignature(transaction.getSign());
        transactionMessage.setTimeStamp(transaction.getTimeStamp());
        return transactionMessage;
    }

    private Protocol generateAddBlockMessage() {
        Block block = blockMiner.mineBlock();
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

    private Protocol generateNotifyNodeMessage() throws
                                                 BlockChainNodeException {
        NotifyNodeMessage notifyNodeMessage = new NotifyNodeMessage();
        BlockChainNodeData data = blockChainNode.start();
        notifyNodeMessage.setBlockChainNode(data);
        return notifyNodeMessage;
    }
}
