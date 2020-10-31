package com.blackhearth.blockchain.block;

import com.blackhearth.blockchain.block.repository.BlockChainRepository;
import com.blackhearth.blockchain.wallet.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BasicBlockBuilder implements BlockBuilder {

    private Block blockInBuildingProcess;
    private final BlockChainRepository repository;
    private final Wallet wallet;

    @Override
    public void addDataToNextBlock(String data) {
        if (blockInBuildingProcess != null) {
            blockInBuildingProcess.setData(blockInBuildingProcess.getData() + "\n" + data);
        } else {
            blockInBuildingProcess = new Block();
            blockInBuildingProcess.setData("MINED" + wallet.getHash());
            blockInBuildingProcess.setTimeStamp(System.currentTimeMillis());
            blockInBuildingProcess.setData(data);
            blockInBuildingProcess.setPreviousHash(repository.getLastBlockHash());
        }
    }

    @Override
    public Block extractBlock() {
        Block block = blockInBuildingProcess;
        blockInBuildingProcess = null;
        return block;
    }
}
