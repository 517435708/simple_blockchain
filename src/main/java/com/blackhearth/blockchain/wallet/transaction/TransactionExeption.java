package com.blackhearth.blockchain.wallet.transaction;

public class TransactionExeption extends Throwable {
    public TransactionExeption(String message) {
        super(message);
    }
}