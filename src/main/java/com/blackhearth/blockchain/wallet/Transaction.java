package com.blackhearth.blockchain.wallet;

import lombok.Data;

@Data
public class Transaction {
    private String address;
    private String amount;
    private String sign;
    private long timeStamp;
}
