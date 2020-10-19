package com.blackhearth.blockchain.protocol.message;

import com.blackhearth.blockchain.node.BlockChainNodeException;

public interface MessageFactory {
    Protocol generateMessages(ProtocolHeader header, String walletAddress) throws
                                                                           BlockChainNodeException;

    Protocol generateMessages(ProtocolHeader header) throws
                                                     BlockChainNodeException;
}
