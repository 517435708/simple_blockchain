package com.blackhearth.blockchain.peertopeer;

import com.blackhearth.blockchain.block.BlockMiner;
import com.blackhearth.blockchain.block.repository.BlockChainRepository;
import com.blackhearth.blockchain.node.BlockChainNodeData;
import com.blackhearth.blockchain.node.BlockChainNodeException;
import com.blackhearth.blockchain.protocol.message.MessageFactory;
import com.blackhearth.blockchain.protocol.message.ProtocolHeader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class BasicPeerToPeerService implements PeerToPeerService {
    private final BlockChainCommunication communication;
    private final PeerToPeerRepository p2pRepository;
    private final BlockChainRepository blockChainRepository;

    private final BlockMiner miner;
    private final MessageFactory messageFactory;

    private int tcpPort = 0;

    private final BlockingQueue<String> messages;
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    @Value("${mining:false}")
    private boolean mining;

    @Override
    public void start(int tcpPort) throws IOException {
        this.tcpPort = tcpPort;
        communication.start(tcpPort);
        if (mining) {
            new Thread(this::runMiner).start();
        }
    }


    @SneakyThrows
    @Override
    public void sendMessageTo(String message, String address, String port) {
        try {
            ////log.info("Sending msg: {} to {}:{}", message, address, port);
            communication.sendTo(message, address, Integer.parseInt(port));
        }catch (Exception e){
            log.error("Failed to send msg: {} to {}:{}", message, address, port);
            log.error(e.getMessage());
            p2pRepository.deleteNode(new BlockChainNodeData(Integer.parseInt(port), address));
        }
    }

    @Override
    public void sendMessageTo(String message, HostInfo hostInfo) {
        sendMessageTo(message, hostInfo.getAddress(), String.valueOf(hostInfo.getPort()));
    }

    @Override
    public void sendMessageToAllKnownNodes(String message) {
        messages.add(message);

        if (!isRunning.getAndSet(true)) {
            while (!messages.isEmpty()) {
                try {
                    String msg = messages.take();
                    new Thread(() -> communication
                            .getAllKnownHosts()
                            .stream().filter(host -> !(host.getIp()
                                                           .equals(IpUtils.getLocalHostLANAddress()
                                                                          .getHostAddress()) && host.getPort() == tcpPort))
                            .forEach(host -> sendMessageTo(msg, host.getIp(), String.valueOf(host.getPort()))))
                            .start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isRunning.set(false);
        }
    }

    @Override
    public BlockChainNodeData getLocalBlockChainNodeData() {
        InetAddress localHostLANAddress = IpUtils.getLocalHostLANAddress();
        return new BlockChainNodeData(tcpPort, localHostLANAddress.getHostAddress());
    }

    private void runMiner() {
        try {
            startMining();
        } catch (Exception ex) {
            log.error(String.valueOf(ex));
        }
    }

    private void startMining() {
        while (true) {
            miner.startMining();
            blockChainRepository.addToBlockChain(miner.lastMinedBlock());
            sendMessageToAllKnownNodes(messageFactory.generateMessages(ProtocolHeader.ADD_BLOCK).generateMessage());
        }
    }
}
