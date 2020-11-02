package com.blackhearth.blockchain.protocol.interpreter;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.BlockBuilder;
import com.blackhearth.blockchain.block.repository.BlockChainRepository;
import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.node.BlockChainNodeException;
import com.blackhearth.blockchain.peertopeer.PeerToPeerRepository;
import com.blackhearth.blockchain.peertopeer.PeerToPeerService;
import com.blackhearth.blockchain.protocol.message.MessageFactory;
import com.blackhearth.blockchain.protocol.message.Protocol;
import com.blackhearth.blockchain.protocol.message.ProtocolHeader;
import com.blackhearth.blockchain.validation.TransactionParams;
import com.blackhearth.blockchain.validation.Validator;
import com.blackhearth.blockchain.wallet.WalletData;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.*;

@Slf4j
@Component
@AllArgsConstructor
public class BasicProtocolInterpreter implements ProtocolInterpreter {

    private final BlockChainRepository blockChainRepository;
    private final PeerToPeerRepository peerToPeerRepository;
    private final Validator validator;
    private final BlockBuilder blockBuilder;
    private final MessageFactory messageFactory;

    private final PeerToPeerService peerToPeerService;

    @Override
    public void interpretMessage(String message, String senderAddress, String senderPort) {
        Optional<ProtocolHeader> optionalProtocolHeader = ProtocolHeader.getFromCode(message.substring(0, 2));
        optionalProtocolHeader.ifPresent(protocolHeader -> proceed(protocolHeader,
                                                                   message.substring(2),
                                                                   senderAddress,
                                                                   senderPort));
    }

    private void proceed(ProtocolHeader protocolHeader, String value, String address, String port) {
        switch (protocolHeader) {
            case NOTIFY_NODE:
                notifyNode(value);
                break;
            case NOTIFY_WALLET:
                notifyWallet(value);
                break;
            case ADD_BLOCK:
                addBlock(value);
                break;
            case TRANSACTION:
                transaction(value);
                break;
            case WALLET_DATA_REQUEST:
                walletDataRequest(value, address, port);
                break;
            case WALLET_DATA_RESPONSE:
                walletDataResponse(value);
                break;
            case WALLETS_REQUEST:
                walletsRequest(address, port);
                break;
            case WALLETS_RESPONSE:
                walletsResponse(value);
                break;
            case NODES_REQUEST:
                nodesRequest(address, port);
                break;
            case NODES_RESPONSE:
                nodesResponse(value);
                break;
            default:
                break;
        }
    }

    private void nodesResponse(String value) {
        BlockChainNodeData[] nodes = new Gson().fromJson(value, BlockChainNodeData[].class);
        for (var node : nodes) {
            peerToPeerRepository.saveNode(node);
        }
    }

    private void nodesRequest(String address, String port) {
        try {
            Protocol protocol = messageFactory.generateMessages(NODES_RESPONSE);
            peerToPeerService.sendMessageTo(protocol.generateMessage(), address, port);
        } catch (BlockChainNodeException e) {
            log.error(String.valueOf(e));
        }
    }

    private void walletsResponse(String value) {
        String[] wallets = value.split("\\|");
        peerToPeerRepository.saveWalletsAddresses(wallets);
    }

    private void walletsRequest(String address, String port) {
        try {
            Protocol protocol = messageFactory.generateMessages(WALLETS_RESPONSE);
            peerToPeerService.sendMessageTo(protocol.generateMessage(), address, port);
        } catch (BlockChainNodeException e) {
            log.error(String.valueOf(e));
        }
    }

    private void walletDataResponse(String value) {
        String[] args = value.split("\\|");
        WalletData walletData = new WalletData();
        walletData.setAmountOfMoney(args[0]);
        walletData.setPublicKey(args[1]);
        walletData.setAddress(args[2]);
        if (!(args[0].equals("") || args[1].equals("") || args[2].equals(""))) {
            peerToPeerRepository.saveWalletData(walletData);
        }
    }

    private void walletDataRequest(String walletAddress, String address, String port) {
        try {
            Protocol protocol = messageFactory.generateMessages(WALLET_DATA_RESPONSE, walletAddress);
            peerToPeerService.sendMessageTo(protocol.generateMessage(), address, port);
        } catch (BlockChainNodeException ex) {
            log.error(String.valueOf(ex));
        }
    }

    private void transaction(String value) {
        String[] args = value.split("\\|");
        TransactionParams transactionParams = new TransactionParams(args[0],
                                                                    args[1],
                                                                    args[2],
                                                                    args[3],
                                                                    Long.parseLong(args[4]));
        if (validator.isTransactionValid(transactionParams)) {
            blockBuilder.addDataToNextBlock(TRANSACTION.getCode() + value);
        }
    }

    private void addBlock(String value) {
        Block block = new Gson().fromJson(value, Block.class);
        if (validator.isBlockValid(block)) {
            blockChainRepository.addToBlockChain(block);
        }
    }

    private void notifyWallet(String value) {
        String[] values = value.split("HASH:");
        if (validator.isWalletValid(values[1], values[0])) {
            blockBuilder.addDataToNextBlock(NOTIFY_WALLET.getCode() + value);
        }
    }

    private void notifyNode(String value) {
        log.info("NotifyNode with value: {}", value);
        peerToPeerRepository.saveNode(new Gson().fromJson(value, BlockChainNodeData.class));
    }
}
