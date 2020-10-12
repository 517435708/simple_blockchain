package com.blackhearth.blockchain.protocol.message;

import com.blackhearth.blockchain.block.Block;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.ADD_BLOCK;

@Setter
@NoArgsConstructor
class AddBlockMessage implements Protocol {

    private Block block;

    @Override
    public String generateMessage() {
        return ADD_BLOCK.getCode() + new Gson().toJson(block);
    }
}
