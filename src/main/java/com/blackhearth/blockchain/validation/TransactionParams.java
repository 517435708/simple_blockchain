package com.blackhearth.blockchain.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TransactionParams {
    private String addressFrom;
    private String addressTo;
    private String transactionMoneyAmount;
    private String digitalSign;
}
