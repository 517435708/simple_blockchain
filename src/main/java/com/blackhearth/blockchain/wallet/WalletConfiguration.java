package com.blackhearth.blockchain.wallet;

import com.blackhearth.blockchain.block.BasicBlockBuilder;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.NOTIFY_WALLET;

@Configuration
@RequiredArgsConstructor
public class WalletConfiguration {

    @Value("${wallet.new:false}")
    private boolean newWallet;

    @Value("${wallet.path:''}")
    private String walletPath;

    @Bean
    public Wallet wallet(BasicBlockBuilder blockBuilder) {
        if (newWallet) {
            Wallet wallet = new Wallet();
            WalletKeysGenerator keyGenerator = new WalletKeysGenerator(2048);
            wallet.setPrivateKey(keyGenerator.getPrivateKey());
            wallet.setPublicKey(keyGenerator.getPublicKey());
            wallet.setHash(new SignatureGenerator().getWalletHash(wallet));
            try (FileWriter fileWriter = new FileWriter(new File(wallet.getHash()))) {
                var simple = new SimpleWallet(wallet);
                fileWriter.append(new Gson().toJson(simple));
                blockBuilder.setWallet(wallet);
                blockBuilder.addDataToNextBlock(NOTIFY_WALLET.getCode() + simple.getPublicKey() + "HASH:" + simple.getWalletAddress());
                return wallet;
            } catch (IOException e) {
                e.printStackTrace();
                blockBuilder.setWallet(new Wallet());
                return new Wallet();
            }
        }
        if (walletPath.isEmpty()) {
            try {
                return new Gson().fromJson(Files.readString(new File(walletPath).toPath()), Wallet.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        blockBuilder.setWallet(new Wallet());
        return new Wallet();
    }

}
