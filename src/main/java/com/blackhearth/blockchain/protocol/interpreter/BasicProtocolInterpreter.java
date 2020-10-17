package com.blackhearth.blockchain.protocol.interpreter;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.BlockBuilder;
import com.blackhearth.blockchain.block.BlockChainRepository;
import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.node.BlockChainNodeException;
import com.blackhearth.blockchain.peertopeer.PeerToPeerRepository;
import com.blackhearth.blockchain.protocol.message.MessageFactory;
import com.blackhearth.blockchain.protocol.message.Protocol;
import com.blackhearth.blockchain.protocol.message.ProtocolHeader;
import com.blackhearth.blockchain.validation.TransactionParams;
import com.blackhearth.blockchain.validation.Validator;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @Override
    public Optional<String> interpretMessage(String message) {
        Optional<ProtocolHeader> optionalProtocolHeader = ProtocolHeader.getFromCode(message.substring(0, 2));
        return optionalProtocolHeader.map(protocolHeader -> proceed(protocolHeader, message.substring(2)));
    }

    private String proceed(ProtocolHeader protocolHeader, String value) {
        switch (protocolHeader) {
            case NOTIFY_NODE: return notifyNode(value);
            case NOTIFY_WALLET: return notifyWallet(value);
            case ADD_BLOCK: return addBlock(value);
            case TRANSACTION: return transaction(value);
            case WALLET_DATA_REQUEST: return walletDataRequest(value);
            case WALLET_DATA_RESPONSE: return walletDataResponse(value);
            case WALLETS_REQUEST: return walletsRequest(value);
            case WALLETS_RESPONSE: return walletsResponse(value);
            case NODES_REQUEST: return nodesRequest(value);
            case NODES_RESPONSE: return nodesResponse(value);
            default: return null;
        }
    }

    private String nodesResponse(String value) {
        return null;
    }

    private String nodesRequest(String value) {
        return null;
    }

    private String walletsResponse(String value) {
        return null;
    }

    private String walletsRequest(String value) {
        return null;
    }

    private String walletDataResponse(String value) {
        return null;
    }

    private String walletDataRequest(String value) {
        try {
            List<Protocol> protocol = messageFactory.generateMessages(WALLET_DATA_RESPONSE, value);
            return protocol.stream().findFirst().orElseThrow(() -> new BlockChainNodeException("No protocol found")).generateMessage();
        } catch (BlockChainNodeException ex) {
            log.error(String.valueOf(ex));
            return null;
        }
    }

    private String transaction(String value) {
        String[] args = value.split("\\|");
        TransactionParams transactionParams = new TransactionParams(args[0], args[1], args[2], args[3]);
        if (validator.isTransactionValid(transactionParams)) {
            blockBuilder.addDataToNextBlock(TRANSACTION.getCode() + value);
        }
        return null;
    }

    private String addBlock(String value) {
        Block block = new Gson().fromJson(value, Block.class);
        if (validator.isBlockValid(block)) {
            blockChainRepository.addToBlockChain(block);
        }
        return null;
    }

    private String notifyWallet(String value) {
        String[] values = value.split("HASH:");
        if (validator.isWalletValid(values[1], values[0])) {
            blockBuilder.addDataToNextBlock(NOTIFY_WALLET.getCode() + value);
        }
        return null;
    }

    private String notifyNode(String value) {
        peerToPeerRepository.saveNode(new Gson().fromJson(value, BlockChainNodeData.class));
        return null;
    }
}
