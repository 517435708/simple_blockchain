package com.blackhearth.blockchain.node;

import com.blackhearth.blockchain.peertopeer.BasicPeerToPeerRepository;
import com.blackhearth.blockchain.peertopeer.HostInfo;
import com.blackhearth.blockchain.peertopeer.IpUtils;
import com.blackhearth.blockchain.peertopeer.PeerToPeerService;
import com.blackhearth.blockchain.protocol.message.BasicMessageFactory;
import com.blackhearth.blockchain.protocol.message.ProtocolHeader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;


@Slf4j
@Component
@RequiredArgsConstructor
public class BlockChainNode {
    private final PeerToPeerService p2pService;
    private final Random random;
    private final BasicMessageFactory messageFactory;
    private final BasicPeerToPeerRepository p2pRepository;

    private boolean isNodeRunning = false;

    @Getter
    private int port;

    @SneakyThrows(IOException.class)
    public BlockChainNodeData start(HostInfo firstKnown) throws
            BlockChainNodeException {
        if (!isNodeRunning) {
            port = discoverPort();
            runNode();
//            runMiner();
//            createWallet();
            sendRequestToFirstKnownHost(firstKnown);
            isNodeRunning = true;
        }

        return composeData();
    }

    public BlockChainNodeData composeData() {
        return p2pService.getLocalBlockChainNodeData();
    }

    private void runNode() throws IOException {
        p2pService.start(port);
        p2pRepository.saveNode(new BlockChainNodeData(port, IpUtils.getLocalHostLANAddress().getHostAddress()));
        log.info("BlockChain started on TCP port {}", port);
    }

    private void runMiner() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    private void createWallet() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @SneakyThrows
    private void sendRequestToFirstKnownHost(HostInfo firstKnownHost) {
        if (firstKnownHost == null) {
            return;
        }

        String requestNodesMessage = messageFactory.generateMessages(ProtocolHeader.NODES_REQUEST).generateMessage();
        p2pService.sendMessageTo(requestNodesMessage, firstKnownHost);
    }

    private int discoverPort() throws BlockChainNodeException {
        int tryNumber = 0;
        do {
            int port = random.ints(1, 49152, 65535)
                    .sum();

            if (IpUtils.isPortAvailable(port)) {
                return port;
            }

            tryNumber++;
        } while (tryNumber > 10);
        throw new BlockChainNodeException("Failed to Create server socket");
    }
}
