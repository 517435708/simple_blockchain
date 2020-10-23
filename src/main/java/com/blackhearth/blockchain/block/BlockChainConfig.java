package com.blackhearth.blockchain.block;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class BlockChainConfig {
    @Bean("blockChain")
    public Map<String, List<Block>> blockchain() {
        Map<String, List<Block>> hashMap = new HashMap<>();
        hashMap.put("", new ArrayList<>());
        return hashMap;
    }
}
