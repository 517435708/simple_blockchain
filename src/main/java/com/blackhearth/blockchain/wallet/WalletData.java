package com.blackhearth.blockchain.wallet;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class
WalletData {
    private String address;
    private String amountOfMoney;
    private String publicKey;
}
