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

    public static HostInfo from(String addressWithPort) {
        try {
            String[] split = addressWithPort.split(":");
            String host = split[0];
            int port = Integer.parseInt(split[1]);
            return new HostInfo(host, port);
        }
        catch (Exception e){
            return null;
        }
    }
}
