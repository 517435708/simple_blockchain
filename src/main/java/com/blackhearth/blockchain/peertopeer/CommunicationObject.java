package com.blackhearth.blockchain.peertopeer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommunicationObject {
    private String text;
    private HostInfo senderInfo;
}
