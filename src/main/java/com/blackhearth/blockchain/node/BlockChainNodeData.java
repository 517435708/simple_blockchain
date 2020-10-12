package com.blackhearth.blockchain.node;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class BlockChainNodeData {
    private int port;
    private String ip;
}
