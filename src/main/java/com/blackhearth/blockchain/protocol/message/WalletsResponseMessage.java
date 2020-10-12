package com.blackhearth.blockchain.protocol.message;

import com.blackhearth.blockchain.wallet.WalletData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.WALLETS_RESPONSE;

@Setter
@NoArgsConstructor
class WalletsResponseMessage implements Protocol {

    private List<String> wallets;
    private String messagePosition;

    @Override
    public String generateMessage() {
        StringBuilder message = new StringBuilder(WALLETS_RESPONSE.getCode());
        for (var wallet : wallets) {
            message.append(wallet).append("|");
        }
        return message.append(messagePosition).toString();
    }
}
