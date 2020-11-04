package com.blackhearth.blockchain.node;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockChainNodeData {
    private int port;
    private String ip;

    @Override
    public String toString() {
        return String.format("BCN{%s:%d}", ip, port);
    }
}
