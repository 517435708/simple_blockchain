package com.blackhearth.blockchain.protocol.message;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.blackhearth.blockchain.protocol.message.ProtocolHeader.WALLETS_RESPONSE;

@Setter
@NoArgsConstructor
class WalletsResponseMessage implements Protocol {

    private List<String> wallets;

    @Override
    public String generateMessage() {
        StringBuilder message = new StringBuilder(WALLETS_RESPONSE.getCode());
        for (int i = 0; i < wallets.size(); i++) {
            message.append(wallets.get(i));
            if (i != wallets.size() - 1) {
                message.append("|");
            }
        }
        return message.toString();
    }
}
