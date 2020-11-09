package com.blackhearth.blockchain;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class Config {
    private static final int BUFFER = 9999;

    @Bean
    Random random() {
        return new Random();
    }

    @Bean
    Client getClient() {
        return new Client(BUFFER, BUFFER);
    }

    @Bean
    Server getServerBean() {
        return new Server(BUFFER, BUFFER);
    }

    @Bean
    BlockingQueue<String> queue() {
        return new LinkedBlockingQueue<>();
    }

}
