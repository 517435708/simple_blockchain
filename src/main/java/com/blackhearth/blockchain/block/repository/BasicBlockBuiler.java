package com.blackhearth.blockchain.block.repository;

import com.blackhearth.blockchain.block.BlockBuilder;
import org.springframework.stereotype.Component;

@Component
public class BasicBlockBuiler implements BlockBuilder {
    @Override
    public void addDataToNextBlock(String data) {
        // TODO: 21.10.2020
    }
}
