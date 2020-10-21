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
import com.blackhearth.blockchain.validation.TransactionParams;
import com.blackhearth.blockchain.validation.Validator;
import com.blackhearth.blockchain.wallet.WalletData;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.*;

class ProtocolInterpreterTest {

    private static ProtocolInterpreter protocolInterpreter;
    private static BlockChainRepository blockChainRepository;
    private static PeerToPeerRepository peerToPeerRepository;
    private static Validator validator;
    private static BlockBuilder blockBuilder;
    private static MessageFactory messageFactory;
    private static PeerToPeerService peerToPeerService;


    @BeforeAll
    public static void init() {

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
        Mockito.reset(blockChainRepository,
                      peerToPeerRepository,
                      validator,
                      blockBuilder,
                      messageFactory,
                      peerToPeerService);
    }


    @Test
    void nodesResponse() {
        //given
        BlockChainNodeData node1 = new BlockChainNodeData(1337, "124.120.120.1");
        BlockChainNodeData node2 = new BlockChainNodeData(2337, "126.120.120.1");
        List<BlockChainNodeData> nodes = Arrays.asList(node1, node2);

        String message = NODES_RESPONSE.getCode() + new Gson().toJson(nodes);
        String senderAddress = "192.168.1.1";
        String senderPort = "2137";

        //when
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(peerToPeerRepository, Mockito.times(1))
               .saveNode(node1);
        Mockito.verify(peerToPeerRepository, Mockito.times(1))
               .saveNode(node2);
    }

    @Test
    void nodesRequest() {
        //given
        Protocol protocol = () -> "Mock";

        String message = NODES_REQUEST.getCode();
        String senderAddress = "192.168.1.1";
        String senderPort = "2137";

        //when
        try {
            Mockito.when(messageFactory.generateMessages(NODES_RESPONSE))
                   .thenReturn(protocol);
        } catch (BlockChainNodeException e) {
            e.printStackTrace();
        }
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(peerToPeerService, Mockito.times(1))
               .sendMessageTo("Mock", senderAddress, senderPort);
    }

    @Test
    void walletsResponse() {
        //given
        String message = WALLETS_RESPONSE.getCode() + "address1|address2|address3";
        String senderAddress = "192.168.1.1";
        String senderPort = "2137";

        //when
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(peerToPeerRepository, Mockito.times(1)).saveWalletsAddresses(Arrays.asList("address1", "address2", "address3").toArray(String[]::new));
    }

    @Test
    void walletsRequest() {
        //given
        Protocol protocol = () -> "Mock";

        String message = WALLETS_REQUEST.getCode();
        String senderAddress = "192.168.1.1";
        String senderPort = "2137";

        //when
        try {
            Mockito.when(messageFactory.generateMessages(WALLETS_RESPONSE))
                   .thenReturn(protocol);
        } catch (BlockChainNodeException e) {
            e.printStackTrace();
        }
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(peerToPeerService, Mockito.times(1))
               .sendMessageTo("Mock", senderAddress, senderPort);
    }
    @Test
    void walletDataResponseNotFound() {
        //given
        String address = "address";

        String message = WALLET_DATA_RESPONSE.getCode()  + "|" + "|" + address;
        String senderAddress = "192.168.1.1";
        String senderPort = "2137";

        //when
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(peerToPeerRepository, Mockito.times(0))
               .saveWalletData(Mockito.any());
    }

    @Test
    void walletDataResponse() {
        //given
        String amountOfMoney = "142.21576";
        String publicKey = "PUBLICKEY";
        String address = "address";

        WalletData walletData = new WalletData();
        walletData.setAmountOfMoney(amountOfMoney);
        walletData.setPublicKey(publicKey);
        walletData.setAddress(address);

        String message = WALLET_DATA_RESPONSE.getCode() + amountOfMoney + "|" + publicKey + "|" + address;
        String senderAddress = "192.168.1.1";
        String senderPort = "2137";

        //when
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(peerToPeerRepository, Mockito.times(1))
               .saveWalletData(walletData);
    }

    @Test
    void walletDataRequest() {
        //given
        String message = WALLET_DATA_REQUEST.getCode() + "ADDRESS";
        String messageToSend = WALLET_DATA_RESPONSE.getCode() + "142.52874" + "|" + "PUBLICKEY" + "|" + message;
        Protocol protocol = () -> messageToSend;

        String senderAddress = "192.168.1.1";
        String senderPort = "2137";

        //when
        try {
            Mockito.when(messageFactory.generateMessages(WALLET_DATA_RESPONSE, "ADDRESS"))
                   .thenReturn(protocol);
        } catch (BlockChainNodeException e) {
            e.printStackTrace();
        }
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(peerToPeerService, Mockito.times(1))
               .sendMessageTo(messageToSend, senderAddress, senderPort);
    }

    @Test
    void transaction() {
        //given
        TransactionParams transactionParams = new TransactionParams("addressFrom",
                                                                    "addressTo",
                                                                    "transactionMoneyAmount",
                                                                    "sign",
                                                                    1259721);
        String message = TRANSACTION.getCode()
                + transactionParams.getAddressFrom() + "|"
                + transactionParams.getAddressTo() + "|"
                + transactionParams.getTransactionMoneyAmount() + "|"
                + transactionParams.getDigitalSign() + "|"
                + transactionParams.getTimeStamp();
        String senderAddress = "192.168.1.1";
        String senderPort = "2137";

        //when
        Mockito.when(validator.isTransactionValid(transactionParams))
               .thenReturn(true);
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(blockBuilder, Mockito.times(1))
               .addDataToNextBlock(message);
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
        Mockito.when(validator.isBlockValid(block))
               .thenReturn(true);
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(blockChainRepository, Mockito.times(1))
               .addToBlockChain(block);
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
        Mockito.when(validator.isWalletValid(hash, address))
               .thenReturn(true);
        protocolInterpreter.interpretMessage(message, senderAddress, senderPort);

        //then
        Mockito.verify(blockBuilder, Mockito.times(1))
               .addDataToNextBlock(message);
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
        Mockito.verify(peerToPeerRepository, Mockito.times(1))
               .saveNode(blockChainNode);
    }
}