package com.blackhearth.blockchain.block;

import com.blackhearth.blockchain.block.repository.BlockChainRepository;
import com.blackhearth.blockchain.wallet.Wallet;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@RequiredArgsConstructor
public class BasicBlockBuilder implements BlockBuilder {

    private final BlockChainRepository repository;
    private Wallet wallet;
    private Block blockInBuildingProcess;

    @Override
    public void addDataToNextBlock(String data) {
        if (blockInBuildingProcess != null) {
            blockInBuildingProcess.setData(blockInBuildingProcess.getData() + "\n" + data);
        } else {
            blockInBuildingProcess = new Block();
            if (!wallet.getHash()
                       .equals("")) {
                blockInBuildingProcess.setData("MINED" + wallet.getHash());
            }
            blockInBuildingProcess.setTimeStamp(System.currentTimeMillis());
            blockInBuildingProcess.setData(blockInBuildingProcess.getData() + "\n" + data);
            blockInBuildingProcess.setPreviousHash(repository.getLastBlockHash());
        }
    }

    @Override
    public Block extractBlock() {
        if (blockInBuildingProcess == null) {
            Block block = new Block();
            if (!wallet.getHash()
                       .equals("")) {
                block.setData("MINED" + wallet.getHash());
            }
            block.setTimeStamp(System.currentTimeMillis());
            block.setPreviousHash(repository.getLastBlockHash());
            return block;
        }
        Block block = blockInBuildingProcess;
        blockInBuildingProcess = null;
        return block;
    }
}
