package com.blackhearth.blockchain.block.repository;

import com.blackhearth.blockchain.block.Block;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.NOTIFY_WALLET;
import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.TRANSACTION;

@Component
@Slf4j
public class BasicBlockChainRepository implements BlockChainRepository {

    private final Pattern transactionPattern = Pattern.compile(TRANSACTION.getCode() + "TS[a-zA-Z0-9]+\\|[a-zA-Z0-9+\\/]+\\|(-?\\d+\\.?\\d*)");
    @Resource(name = "blockChain")
    private List<List<Block>> blockChain;

    @Override
    public Optional<String> getCoinsFromAddress(String walletAddress) {
        List<Block> longestChain = extractLongestChain();
        return Optional.of(String.valueOf(getAmountOfCoinsFromTransactions(walletAddress, longestChain)));
    }

    @Override
    public synchronized List<Block> getChainToBlockHash(String hash) {
        for (var chain : blockChain) {
            if (!chain.isEmpty() && chain.get(chain.size() - 1)
                                         .getHash()
                                         .equals(hash)) {
                return chain;
            }
        }

        for (var chain : blockChain) {
            for (var block : chain) {
                if (block.getHash()
                         .equals(hash)) {
                    blockChain.add(Collections.synchronizedList(new ArrayList<>(chain.subList(0,
                                                                                              chain.indexOf(block)))));
                    return blockChain.get(blockChain.size() - 1);
                }
            }
        }

        for (var chain : blockChain) {
            if (chain.isEmpty()) {
                return chain;
            }
        }

        blockChain.add(Collections.synchronizedList(new ArrayList<>()));
        return blockChain.get(blockChain.size() - 1);
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
        log.debug("adding to blockchain {}", block);
        var chain = getChainToBlockHash(block.getPreviousHash());
        chain.add(block);
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
    public synchronized List<Block> extractLongestChain() {
        int max = 0;
        List<Block> chainToReturn = blockChain.get(0);
        for (var chain : blockChain) {
            if (chain.size() > max) {
                max = chain.size();
                chainToReturn = chain;
            }
        }
        return chainToReturn;
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
