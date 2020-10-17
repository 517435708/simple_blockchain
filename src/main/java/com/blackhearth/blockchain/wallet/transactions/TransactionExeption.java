package com.blackhearth.blockchain.wallet.transactions;

public class TransactionExeption extends Throwable {
    public TransactionExeption(String message) {
        super(message);
    }
}