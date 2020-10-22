package com.blackhearth.blockchain.peertopeer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class HostInfo {
    private String address;
    private int port;
}
