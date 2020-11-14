package com.blackhearth.blockchain.block;

import com.blackhearth.blockchain.block.repository.BlockChainRepository;
import com.blackhearth.blockchain.wallet.Wallet;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Setter
@Component
public class BasicBlockBuilder implements BlockBuilder {

    private Wallet wallet;
    private Block blockInBuildingProcess;

    @Override
    public void addDataToNextBlock(String data) {
        log.info("ADDING DATA TO BLOCK: " + data);
        if (blockInBuildingProcess == null) {
            blockInBuildingProcess = new Block();
            if (!wallet.getHash()
                       .equals("")) {
                blockInBuildingProcess.setData("MINED" + wallet.getHash());
            }
            blockInBuildingProcess.setTimeStamp(System.currentTimeMillis());
        }
        blockInBuildingProcess.setData(blockInBuildingProcess.getData() + "\n" + data);
    }

    @Override
    public Block extractBlock() {
        log.info("extracting block {}", blockInBuildingProcess);
        if (blockInBuildingProcess == null) {
            Block block = new Block();
            if (!wallet.getHash()
                       .equals("")) {
                block.setData("MINED" + wallet.getHash());
            }
            block.setTimeStamp(System.currentTimeMillis());
            return block;
        }
        Block block = blockInBuildingProcess;
        blockInBuildingProcess = null;
        return block;
    }
}
