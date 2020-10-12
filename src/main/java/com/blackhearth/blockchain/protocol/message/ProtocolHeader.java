package com.blackhearth.blockchain.protocol.message;

import lombok.Getter;

public enum ProtocolHeader {

    NOTIFY_NODE("NN"),
    NOTIFY_WALLET("NW"),
    ADD_BLOCK("AB"),
    TRANSACTION("TS"),
    WALLET_DATA_REQUEST("WD"),
    WALLET_DATA_RESPONSE("WR"),
    WALLETS_REQUEST("WS"),
    WALLETS_RESPONSE("WE"),
    NODES_REQUEST("NS"),
    NODES_RESPONSE("NR");


    ProtocolHeader(String code) {
        this.code = code;
    }

    @Getter
    private final String code;

}
