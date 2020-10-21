package com.blackhearth.blockchain.params_reader;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class Pair<K, V> implements Serializable {
    private K key;
    private V value;
}
