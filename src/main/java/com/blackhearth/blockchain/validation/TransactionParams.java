package com.blackhearth.blockchain.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class TransactionParams {
    private String addressFrom;
    private String addressTo;
    private String transactionMoneyAmount;
    private String digitalSign;
    private long timeStamp;
}
