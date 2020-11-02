package com.blackhearth.blockchain;

import com.blackhearth.blockchain.node.BlockChainNode;
import com.blackhearth.blockchain.paramsreader.ParamsReader;
import com.blackhearth.blockchain.paramsreader.RunParams;
import com.blackhearth.blockchain.peertopeer.HostInfo;
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
		String firstKnownHost = String.valueOf(params.getOrDefault(RunParams.FIRST_KNOWN, "192.168.0.100:51542"));

		BlockChainNode blockChainNode = (BlockChainNode) run.getBean("blockChainNode");
		blockChainNode.start(HostInfo.from(firstKnownHost));
	}
}
