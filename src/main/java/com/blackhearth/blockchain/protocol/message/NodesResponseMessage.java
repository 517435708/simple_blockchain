package com.blackhearth.blockchain.protocol.message;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.NODES_RESPONSE;

@Setter
@NoArgsConstructor
class NodesResponseMessage implements Protocol{

    private List<BlockChainNodeData> nodes;
    private String messagePosition;

    @Override
    public String generateMessage() {
        StringBuilder message = new StringBuilder(NODES_RESPONSE.getCode());
        Gson gson = new Gson();
        for (var node : nodes) {
            message.append(gson.toJson(node));
        }
        return message.append(messagePosition).toString();
    }
}
