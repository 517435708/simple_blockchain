package com.blackhearth.blockchain.protocol.interpreter;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.BlockBuilder;
import com.blackhearth.blockchain.block.BlockChainRepository;
import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.node.BlockChainNodeException;
import com.blackhearth.blockchain.peertopeer.PeerToPeerRepository;
import com.blackhearth.blockchain.peertopeer.PeerToPeerService;
import com.blackhearth.blockchain.protocol.message.MessageFactory;
import com.blackhearth.blockchain.protocol.message.Protocol;
import com.blackhearth.blockchain.validation.TransactionParams;
import com.blackhearth.blockchain.validation.Validator;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.*;

class ProtocolInterpreterTest {

    private ProtocolInterpreter protocolInterpreter;
    private BlockChainRepository blockChainRepository;
    private PeerToPeerRepository peerToPeerRepository;
    private Validator validator;
    private BlockBuilder blockBuilder;
    private MessageFactory messageFactory;
    private PeerToPeerService peerToPeerService;


    @BeforeAll
    public void init() {

        blockChainRepository = Mockito.mock(BlockChainRepository.class);
        peerToPeerRepository = Mockito.mock(PeerToPeerRepository.class);
        validator = Mockito.mock(Validator.class);
        blockBuilder = Mockito.mock(BlockBuilder.class);
        messageFactory = Mockito.mock(MessageFactory.class);
        peerToPeerService = Mockito.mock(PeerToPeerService.class);

        protocolInterpreter = new BasicProtocolInterpreter(blockChainRepository,
                                                           peerToPeerRepository,
                                                           validator,
                                                           blockBuilder,
                                                           messageFactory,
                                                           peerToPeerService);
    }

    @AfterEach
    public void reset() {
        Mockito.reset(blockChainRepository, peerToPeerRepository, validator, blockBuilder, messageFactory, peerToPeerService);
    }



    @Test
    void nodesResponse() {

    }

    @Test
    void nodesRequest() {

    }

    @Test
    void walletsResponse() {

    }

    @Test
    void walletsRequest() {

    }

    @Test
    void walletDataResponse() {

    }

    @Test
    void walletDataRequest() {
        //given
        String message = WALLET_DATA_REQUEST.getCode() + "ADDRESS";
        String messageToSend = WALLET_DATA_RESPONSE.getCode() + "142.52874" + "|" + "PUBLICKEY" + "|" + message;
        String senderAddress = "192.168.1.1";
        String senderPort = "2137";

        //when
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(peerToPeerService, Mockito.times(1)).sendMessageTo(messageToSend, senderAddress, senderPort);
    }

    @Test
    void transaction() {
        //given
        TransactionParams transactionParams = new TransactionParams("addressFrom", "addressTo", "transactionMoneyAmount", "sign");
        String message = TRANSACTION.getCode() + transactionParams.getAddressFrom() + "|" + transactionParams.getAddressTo() + "|" + transactionParams.getTransactionMoneyAmount() + "|" + transactionParams.getDigitalSign();
        String senderAddress = "192.168.1.1";
        String senderPort = "2137";

        //when
        Mockito.when(validator.isTransactionValid(transactionParams)).thenReturn(true);
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(blockBuilder, Mockito.times(1)).addDataToNextBlock(message);
    }


    @Test
    void addBlock() {
        //given
        Block block = new Block();
        block.setHash("HASH");
        block.setData("DATA");
        block.setTimeStamp(13372137);

        String message = ADD_BLOCK.getCode() + new Gson().toJson(block);
        String senderAddress = "192.168.1.1";
        String senderPort = "2137";

        //when
        Mockito.when(validator.isBlockValid(block)).thenReturn(true);
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(blockChainRepository, Mockito.times(1)).addToBlockChain(block);
    }


    @Test
    void notifyWallet() {
        //given
        String hash = "hash";
        String address = "address";
        String message = NOTIFY_WALLET.getCode() + address + "HASH:" + hash;
        String senderAddress = "192.168.1.1";
        String senderPort = "2137";

        //when
        Mockito.when(validator.isWalletValid(hash, address)).thenReturn(true);
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(blockBuilder, Mockito.times(1)).addDataToNextBlock(message);
    }

    @Test
    void notifyNode() {
        //given
        BlockChainNodeData blockChainNode = new BlockChainNodeData(1337,
                                                                   "127.0.0.1");
        String message = NOTIFY_NODE.getCode() + new Gson().toJson(blockChainNode);
        String senderAddress = "192.168.1.1";
        String senderPort = "2137";
        //when
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(peerToPeerRepository, Mockito.times(1)).saveNode(blockChainNode);
    }
}