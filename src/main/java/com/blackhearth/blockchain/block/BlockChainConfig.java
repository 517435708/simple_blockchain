package com.blackhearth.blockchain.block;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BlockChainConfig {
    @Bean("blockChain")
    public List<Block> blockchain() {
        return new ArrayList<>();
    }
}
