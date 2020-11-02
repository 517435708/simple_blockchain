package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.node.BlockChainNodeData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;

@Slf4j
@Component
@RequiredArgsConstructor
public class BasicPeerToPeerService implements PeerToPeerService {
    private final BlockChainCommunication communication;
    private int tcpPort = 0;

    @Override
    public void start(int tcpPort) throws IOException {
        this.tcpPort = tcpPort;
        communication.start(tcpPort);
    }

    @SneakyThrows
    @Override
    public void sendMessageTo(String message, String address, String port) {
        log.info("Sending msg: {} to {}:{}", message, address, port);
        communication.sendTo(message, address, Integer.parseInt(port));
    }

    @Override
    public void sendMessageTo(String message, HostInfo hostInfo) {
        sendMessageTo(message, hostInfo.getAddress(), String.valueOf(hostInfo.getPort()));
    }

    @Override
    public void sendMessageToAllKnownNodes(String message) {
        communication.getAllKnownHosts()
                .forEach(host -> sendMessageTo(message, host.getAddress(), String.valueOf(host.getPort())));
    }

    @Override
    public BlockChainNodeData getLocalBlockChainNodeData() {
        InetAddress localHostLANAddress = IpUtils.getLocalHostLANAddress();
        return new BlockChainNodeData(tcpPort, localHostLANAddress.getHostAddress());
    }
}
