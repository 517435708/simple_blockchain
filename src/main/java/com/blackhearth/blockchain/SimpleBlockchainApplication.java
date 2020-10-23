package com.blackhearth.blockchain;

import com.blackhearth.blockchain.node.BlockChainNode;
import com.blackhearth.blockchain.paramsreader.ParamsReader;
import com.blackhearth.blockchain.paramsreader.RunParams;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@SpringBootApplication
public class SimpleBlockchainApplication {

	@SneakyThrows
	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(SimpleBlockchainApplication.class, args);
		Map<RunParams, Object> params = ParamsReader.getParams(args);

		BlockChainNode blockChainNode = (BlockChainNode) run.getBean("blockChainNode");
		blockChainNode.start();

//		BasicPeerToPeerService basicPeerToPeerService = new BasicPeerToPeerService(new InterpreterBlockChainCommunication());
//		basicPeerToPeerService.start(1234);

//		System.out.println(basicPeerToPeerService.getLocalBlockChainNodeData());
	}
}
