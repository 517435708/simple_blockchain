package com.blackhearth.blockchain.protocol.message;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.NOTIFY_NODE;


@Setter
@NoArgsConstructor
class NotifyNodeMessage implements Protocol{

    private BlockChainNodeData blockChainNode;

    @Override
    public String generateMessage() {
        return NOTIFY_NODE.getCode() + new Gson().toJson(blockChainNode);
    }
}
