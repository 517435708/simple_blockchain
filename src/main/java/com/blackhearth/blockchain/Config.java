package com.blackhearth.blockchain;

import com.blackhearth.blockchain.block.BasicBlockMiner;
import com.blackhearth.blockchain.block.BlockBuilder;
import com.blackhearth.blockchain.block.BlockMiner;
import com.blackhearth.blockchain.block.repository.BasicBlockBuiler;
import com.blackhearth.blockchain.validation.ChainValidator;
import com.blackhearth.blockchain.validation.Validator;
import com.blackhearth.blockchain.wallet.Wallet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class Config {


    @Bean
    Random random() {
        return new Random();
    }

    @Bean
    Validator validator() { return new ChainValidator(); }

    @Bean
    BlockBuilder blockBuilder() {return new BasicBlockBuiler(); }

    @Bean
    BlockMiner blockMiner() { return new BasicBlockMiner(); }

    @Bean
    Wallet wallet() { return new Wallet(); }
}
