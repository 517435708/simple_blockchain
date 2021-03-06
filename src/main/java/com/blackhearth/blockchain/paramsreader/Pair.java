package com.blackhearth.blockchain.paramsreader;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Pair<K, V> {
    private K key;
    private V value;
}
