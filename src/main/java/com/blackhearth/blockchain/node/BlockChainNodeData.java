package com.blackhearth.blockchain.node;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockChainNodeData {
    private int port;
    private String ip;
}
