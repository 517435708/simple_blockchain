package com.blackhearth.blockchain.wallet.transaction;

import com.blackhearth.blockchain.wallet.signature.SignatureApplier;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;

@Component
@NoArgsConstructor
public class BasicTransactionFactory implements TransactionFactory {

    @Override
    @SneakyThrows
    public String addSignature(PrivateKey privateKey, Transaction transaction) {
        if (transaction.getAddress() != null && transaction.getSender() != null
                && transaction.getReciepient() != null && transaction.getAmount() > 0L) {
            return SignatureApplier.applySignature(privateKey, transaction.getTransactionData());
        } else {
            throw new TransactionExeption("Something went wrong while adding signature! ;(");
        }
    }
}
