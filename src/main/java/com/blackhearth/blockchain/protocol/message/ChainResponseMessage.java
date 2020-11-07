package com.blackhearth.blockchain.protocol.message;

import com.blackhearth.blockchain.block.Block;
import com.google.gson.Gson;
import lombok.Setter;

import java.util.List;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.CHAIN_RESPONSE;

@Setter
public class ChainResponseMessage implements Protocol {

    private List<Block> chain;

    @Override
    public String generateMessage() {
        return CHAIN_RESPONSE.getCode() + new Gson().toJson(chain);
    }
}
