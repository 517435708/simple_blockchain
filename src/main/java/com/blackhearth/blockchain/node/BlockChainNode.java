package com.blackhearth.blockchain.node;

import com.blackhearth.blockchain.peertopeer.PeerToPeerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;


@Slf4j
@Component
@RequiredArgsConstructor
public class BlockChainNode {
    private final Random random;

    private boolean isNodeRunning = false;
    @Getter
    private ServerSocket socket;

    @Autowired
    private PeerToPeerService p2pService;
    private int port;

    @SneakyThrows(IOException.class)
    public BlockChainNodeData start() throws
            BlockChainNodeException {
        if (!isNodeRunning) {
            socket = openSocket();
            runNode();
            isNodeRunning = true;
        }
        return composeData();
    }

    private BlockChainNodeData composeData() {
        return new BlockChainNodeData(port, socket.getInetAddress().toString());
    }

    private void runNode() throws IOException {
        p2pService.start(port);
        //stuff
    }

    private ServerSocket openSocket() throws
                                      BlockChainNodeException {

        int tryNumber = 0;
        do {
            try {
                port = random.ints(1, 49152, 65535)
                             .sum();
                return new ServerSocket(port);
            } catch (IOException e) {
                tryNumber++;
                e.printStackTrace();
                log.error("Couldn't create server socket, trying again [{}] cause: {}", tryNumber, e.getMessage());
            }
        } while (tryNumber > 10);
        throw new BlockChainNodeException("Failed to Create server socket");
    }
}
