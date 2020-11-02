package com.blackhearth.blockchain;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
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
    Client getClient() {
        return new Client();
    }

    @Bean
    Server getServerBean() {
        return new Server();
    }
}
