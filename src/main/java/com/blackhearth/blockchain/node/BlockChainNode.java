package com.blackhearth.blockchain.node;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private int port;


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

    private void runNode() {
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
                log.error("Couldn't create server socket, trying again [" + tryNumber + "] cause: " + e.getMessage());
            }
        } while (tryNumber > 10);
        throw new BlockChainNodeException("Failed to Create server socket");
    }
}
