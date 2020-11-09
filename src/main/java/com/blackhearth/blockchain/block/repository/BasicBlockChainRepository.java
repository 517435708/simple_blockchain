package com.blackhearth.blockchain.block.repository;

import com.blackhearth.blockchain.block.Block;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.NOTIFY_WALLET;
import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.TRANSACTION;

@Component
@Slf4j
public class BasicBlockChainRepository implements BlockChainRepository {

    private final Pattern transactionPattern = Pattern.compile(TRANSACTION.getCode() + "[a-zA-Z0-9]+\\|[a-zA-Z0-9]+\\|(-?\\d+\\.?\\d*)");
    @Resource(name = "blockChain")
    private Map<String, Collection<Block>> blockChain;


    @PostConstruct
    private void printBlochchain() {
        new Thread(() -> {
            while (true) {
                try {
                    log.info("START Blockchain dump.");
                    blockChain.forEach((k,v) -> log.info("'{}':{}", k, v));
                    log.info("END Blockchain dump.");
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public Optional<String> getCoinsFromAddress(String walletAddress) {

        List<Block> longestChain = extractLongestChain();
        if (walletNotRegistered(walletAddress, longestChain)) {
            return Optional.empty();
        }

        return Optional.of(String.valueOf(getAmountOfCoinsFromTransactions(walletAddress, longestChain)));
    }

    @Override
    public List<Block> getChainToBlockHash(String hash) {
        for (var entry : blockChain.entrySet()) {
            List<Block> chain = new ArrayList<>(entry.getValue());
            if (chain.get(chain.size() - 1)
                     .getHash()
                     .equals(hash)) {
                return chain;
            }
        }

        for (var entry : blockChain.entrySet()) {
            for (var block : entry.getValue()) {
                if (block.getHash()
                         .equals(hash)) {
                    blockChain.put(hash,
                                   new ArrayList<>(entry.getValue())
                                        .subList(0,
                                                 new ArrayList<>(entry.getValue())
                                                      .indexOf(block)));
                    return new ArrayList<>(blockChain.get(hash));
                }
            }
        }

        return Collections.emptyList();
    }

    @Override
    public Optional<String> getPublicKeyFromAddress(String address) {
        List<Block> longestChain = extractLongestChain();

        return Optional.ofNullable(searchThroughChain(longestChain, null, row -> {
            if (row.contains(NOTIFY_WALLET.getCode()) && row.contains(address)) {
                String[] values = row.split("HASH:");
                return values[0].substring(2);
            } else {
                return null;
            }
        }));
    }

    @Override
    public List<String> getWallets() {
        List<Block> longestChain = extractLongestChain();
        List<String> wallets = new ArrayList<>();
        searchThroughChain(longestChain, null, row -> {
            if (row.contains(NOTIFY_WALLET.getCode())) {
                String[] args = row.split("HASH:");
                wallets.add(args[0].substring(2));
            }
            return null;
        });
        return wallets;
    }

    @Override
    public void addToBlockChain(Block block) {
        var chain = getChainToBlockHash(block.getPreviousHash());
        if (chain.isEmpty()) {
            blockChain.put(block.getPreviousHash(), Collections.synchronizedCollection(new ArrayList<>()));
            blockChain.get(block.getPreviousHash()).add(block);
        } else if (!chain.contains(block)) {
            chain.add(block);
        }

    }

    @Override
    public String getLastBlockHash() {
        var list = extractLongestChain();
        if (list.isEmpty()) {
            return "";
        }
        return list.get(list.size() - 1)
                   .getHash();
    }

    @Override
    public List<Block> extractLongestChain() {

        if (blockChain.isEmpty()) {
            return Collections.emptyList();
        }

        String hash = "";
        int maxSize = 0;
        for (var hashByChain : blockChain.entrySet()) {
            int size = hashByChain.getValue()
                                  .size();
            if (maxSize < size) {
                hash = hashByChain.getKey();
                maxSize = size;
            }
        }
        for (var hashByChain : blockChain.entrySet()) {
            int size = hashByChain.getValue()
                                  .size();
            if (maxSize - size > 5) {
                blockChain.remove(hashByChain.getKey());
            }
        }
        return new ArrayList<>(blockChain.get(hash));
    }

    private boolean walletNotRegistered(String walletAddress, List<Block> longestChain) {
        return searchThroughChain(longestChain, false, record -> {
            if (record.contains(NOTIFY_WALLET.getCode()) && record.contains(walletAddress)) {
                return true;
            } else {
                return null;
            }
        });
    }


    private Double getAmountOfCoinsFromTransactions(String address, List<Block> longestChain) {

        double value = 0;

        for (var block : longestChain) {
            String[] data = block.getData()
                                 .split("\\n");
            value += Stream.of(data)
                           .filter(string -> string.contains(address))
                           .map(transactionPattern::matcher)
                           .filter(Matcher::find)
                           .mapToDouble(matcher -> Double.parseDouble(matcher.group(1)))
                           .sum();
            value += Stream.of(data)
                           .filter(string -> string.contains("MINED" + address))
                           .count();
        }

        return value;
    }

    private <T> T searchThroughChain(List<Block> chain, T defaultValue, Function<String, T> function) {
        for (var block : chain) {
            for (var row : block.getData()
                                .split("\\n")) {
                Optional<T> value = Optional.ofNullable(function.apply(row));
                if (value.isPresent()) {
                    return value.get();
                }
            }
        }
        return defaultValue;
    }
}
