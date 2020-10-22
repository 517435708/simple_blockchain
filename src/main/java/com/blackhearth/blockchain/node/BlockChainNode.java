package com.blackhearth.blockchain.node;

import com.blackhearth.blockchain.peertopeer.IpUtils;
import com.blackhearth.blockchain.peertopeer.PeerToPeerService;
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

    private boolean isNodeRunning = false;

    @Getter
    private int port;

    @SneakyThrows(IOException.class)
    public BlockChainNodeData start() throws
            BlockChainNodeException {
        if (!isNodeRunning) {
            port = discoverPort();
            runNode();
            isNodeRunning = true;
        }
        return composeData();
    }

    private BlockChainNodeData composeData() {
        return p2pService.getLocalBlockChainNodeData();
    }

    private void runNode() throws IOException {
        p2pService.start(port);
        log.info("BlockChain started on TCP port {}", port);
        //stuff
    }

    private int discoverPort() throws
            BlockChainNodeException {
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
