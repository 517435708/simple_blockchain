package com.blackhearth.blockchain.block;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.security.MessageDigest;

import static java.nio.charset.StandardCharsets.UTF_8;

@Data
@NoArgsConstructor
public class Block {
    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;

    Block(String data, String previousHash, long timeStamp) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = calculateBlockHash();
    }

    @SneakyThrows
    public String calculateBlockHash() {
        String dataToHash = previousHash
                + timeStamp
                + nonce
                + data;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(dataToHash.getBytes(UTF_8));
        StringBuilder buffer = new StringBuilder();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }

    public void incrementNonce() {
        nonce++;
    }
}