package com.blackhearth.blockchain.protocol.message;

import com.blackhearth.blockchain.node.BlockChainNodeException;

import java.util.List;

public interface MessageFactory {
    List<Protocol> generateMessages(ProtocolHeader header, String walletAddress) throws
                                                                                 BlockChainNodeException;

    List<Protocol> generateMessages(ProtocolHeader header) throws
                                                           BlockChainNodeException;
}
