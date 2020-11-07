package com.blackhearth.blockchain.node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BlockChainNodeData {
    private final int port;
    private final String ip;

    private transient boolean deleted;

    @Override
    public String toString() {
        return String.format("BCN{%s:%d,%s}", ip, port, String.valueOf(deleted));
    }
}
