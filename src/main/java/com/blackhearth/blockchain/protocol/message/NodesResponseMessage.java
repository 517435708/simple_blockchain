package com.blackhearth.blockchain.protocol.message;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.NODES_RESPONSE;

@Setter
@NoArgsConstructor
class NodesResponseMessage implements Protocol {

    private List<BlockChainNodeData> nodes;

    @Override
    public String generateMessage() {
        return NODES_RESPONSE.getCode() + new Gson().toJson(nodes);
}
}
