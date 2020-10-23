package com.blackhearth.blockchain.protocol.message;

import com.blackhearth.blockchain.block.Block;
import com.blackhearth.blockchain.block.repository.BlockChainRepository;
import com.blackhearth.blockchain.block.BlockMiner;
import com.blackhearth.blockchain.node.BlockChainNode;
import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.node.BlockChainNodeException;
import com.blackhearth.blockchain.wallet.Wallet;
import com.blackhearth.blockchain.wallet.WalletData;
import com.blackhearth.blockchain.wallet.transaction.Transaction;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MessageFactoryTest {

    private static MessageFactory messageFactory;
    private static BlockMiner blockMiner;
    private static BlockChainNode blockChainNode;
    private static BlockChainRepository blockChainRepository;
    private static Wallet wallet;

    @BeforeAll
    public static void init() {
        blockMiner = Mockito.mock(BlockMiner.class);
        blockChainNode = Mockito.mock(BlockChainNode.class);
        blockChainRepository = Mockito.mock(BlockChainRepository.class);
        wallet = Mockito.mock(Wallet.class);

        messageFactory = new BasicMessageFactory(blockMiner, blockChainNode, blockChainRepository, wallet);
    }

    @AfterEach
    public void reset() {
        Mockito.reset(blockMiner, blockChainNode, blockChainRepository, wallet);
    }

    @Test
    void notifyNode() throws
                      BlockChainNodeException {
        //given
        BlockChainNodeData blockChainNodeData = new BlockChainNodeData(1337, "123.125.1.1");
        ProtocolHeader header = ProtocolHeader.NOTIFY_NODE;

        //when
        Mockito.when(blockChainNode.start()).thenReturn(blockChainNodeData);
        Protocol protocol = messageFactory.generateMessages(header);

        //then
        assertThat(protocol.generateMessage()).isEqualTo(header.getCode() + new Gson().toJson(blockChainNodeData));
        assertThat(protocol).isInstanceOf(NotifyNodeMessage.class);
    }

    @Test
    void notifyWallet() throws
                        BlockChainNodeException {
        //given
        ProtocolHeader header = ProtocolHeader.NOTIFY_WALLET;
        String hash = "hash";
        PublicKey publicKey = new PublicKey() {
            @Override
            public String getAlgorithm() {
                return null;
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return new byte[0];
            }

            @Override
            public String toString() {
                return "Mock";
            }
        };

        //when
        Mockito.when(wallet.getPublicKey()).thenReturn(publicKey);
        Mockito.when(wallet.getHash()).thenReturn(hash);
        Protocol protocol = messageFactory.generateMessages(header);

        //then
        assertThat(protocol.generateMessage()).isEqualTo(header.getCode() + "MockHASH:hash");
        assertThat(protocol).isInstanceOf(NotifyWalletMessage.class);
    }

    @Test
    void addBlock() throws
                    BlockChainNodeException {
        //given
        Block block = new Block();
        ProtocolHeader header = ProtocolHeader.ADD_BLOCK;

        //when
        Mockito.when(blockMiner.mineBlock()).thenReturn(block);
        Protocol protocol = messageFactory.generateMessages(header);

        //then
        assertThat(protocol.generateMessage()).isEqualTo(header.getCode() + new Gson().toJson(block));
        assertThat(protocol).isInstanceOf(AddBlockMessage.class);
    }

    @Test
    void transaction() throws
                       BlockChainNodeException {
        //given
        String hash = "hash";

        Transaction transaction = new Transaction();
        transaction.setAddress("adres");
        transaction.setAmount("12.12");
        transaction.setSignature("sign");
        transaction.setTimeStamp(1337);

        TransactionMessage transactionMessage = new TransactionMessage();
        transactionMessage.setSenderAddress(hash);
        transactionMessage.setAmountOfCoinTransferred(transaction.getAmount());
        transactionMessage.setReceiverAddress(transaction.getAddress());
        transactionMessage.setDigitalSignature(transaction.getSignature());
        transactionMessage.setTimeStamp(transaction.getTimeStamp());

        ProtocolHeader header = ProtocolHeader.TRANSACTION;

        //when
        Mockito.when(wallet.getHash()).thenReturn(hash);
        Mockito.when(wallet.getLastTransaction()).thenReturn(transaction);
        Protocol protocol = messageFactory.generateMessages(header);

        //then
        assertThat(protocol.generateMessage()).isEqualTo(header.getCode() + "hash|adres|12.12|1337|sign");
        assertThat(protocol).isInstanceOf(TransactionMessage.class);
    }

    @Test
    void walletDataRequest() throws
                             BlockChainNodeException {
        //given
        String hash = "hash";
        ProtocolHeader header = ProtocolHeader.WALLET_DATA_REQUEST;

        //when
        Mockito.when(wallet.getHash()).thenReturn(hash);
        Protocol protocol = messageFactory.generateMessages(header);

        //then
        assertThat(protocol.generateMessage()).isEqualTo(header.getCode() + hash);
        assertThat(protocol).isInstanceOf(WalletDataRequestMessage.class);
    }

    @Test
    void walletDataResponse() throws
                              BlockChainNodeException {
        //given
        String address = "hash";

        ProtocolHeader header = ProtocolHeader.WALLET_DATA_RESPONSE;

        //when
        Mockito.when(blockChainRepository.getCoinsFromAddress(address)).thenReturn(Optional.of("125.354"));
        Mockito.when(blockChainRepository.getPublicKeyFromAddress(address)).thenReturn(Optional.of("publicKey"));
        Protocol protocol = messageFactory.generateMessages(header, address);

        //then
        assertThat(protocol.generateMessage()).isEqualTo(header.getCode() + "125.354|publicKey|" + address);
        assertThat(protocol).isInstanceOf(WalletDataResponseMessage.class);
    }

    @Test
    void walletsRequest() throws
                          BlockChainNodeException {
        //given
        ProtocolHeader header = ProtocolHeader.WALLETS_REQUEST;

        //when
        Protocol protocol = messageFactory.generateMessages(header);

        //then
        assertThat(protocol.generateMessage()).isEqualTo(header.getCode());
        assertThat(protocol).isInstanceOf(WalletsRequestMessage.class);
    }

    @Test
    void walletsResponse() throws
                           BlockChainNodeException {
        //given
        WalletData walletData1 = new WalletData();
        WalletData walletData2 = new WalletData();

        walletData1.setAddress("address1");
        walletData2.setAddress("address2");

        List<WalletData> data = Arrays.asList(walletData1, walletData2);

        ProtocolHeader header = ProtocolHeader.WALLETS_RESPONSE;

        //when
        Mockito.when(blockChainRepository.getWallets()).thenReturn(data);
        Protocol protocol = messageFactory.generateMessages(header);

        //then
        assertThat(protocol.generateMessage()).isEqualTo(header.getCode() + "address1|address2");
        assertThat(protocol).isInstanceOf(WalletsResponseMessage.class);
    }

    @Test
    void nodesRequest() throws
                        BlockChainNodeException {
        //given
        ProtocolHeader header = ProtocolHeader.NODES_REQUEST;

        //when
        Protocol protocol = messageFactory.generateMessages(header);

        //then
        assertThat(protocol.generateMessage()).isEqualTo(header.getCode());
        assertThat(protocol).isInstanceOf(NodesRequestMessage.class);
    }

    @Test
    void nodesResponse() throws
                         BlockChainNodeException {
        //given
        BlockChainNodeData node1 = new BlockChainNodeData(1243, "124.252.65.12");
        BlockChainNodeData node2 = new BlockChainNodeData(1246, "14.22.75.22");
        List<BlockChainNodeData> data = Arrays.asList(node1, node2);

        ProtocolHeader header = ProtocolHeader.NODES_RESPONSE;

        //when
        Mockito.when(blockChainRepository.getNodes()).thenReturn(data);
        Protocol protocol = messageFactory.generateMessages(header);

        //then
        assertThat(protocol.generateMessage()).isEqualTo(header.getCode() + new Gson().toJson(data));
        assertThat(protocol).isInstanceOf(NodesResponseMessage.class);
    }

}