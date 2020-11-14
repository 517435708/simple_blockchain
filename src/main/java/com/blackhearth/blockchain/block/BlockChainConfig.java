package com.blackhearth.blockchain.block;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class BlockChainConfig {
    @Bean("blockChain")
    public List<List<Block>>blockchain() {
        List<Block> chain = Collections.synchronizedList(new ArrayList<>());
        List<List<Block>> blockChain = Collections.synchronizedList(new ArrayList<>());
        blockChain.add(chain);
        return blockChain;
    }
}
