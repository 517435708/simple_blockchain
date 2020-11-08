package com.blackhearth.blockchain;

import com.blackhearth.blockchain.node.BlockChainNode;
import com.blackhearth.blockchain.paramsreader.ParamsReader;
import com.blackhearth.blockchain.paramsreader.RunParams;
import com.blackhearth.blockchain.peertopeer.HostInfo;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SimpleBlockchainApplication {

	@SneakyThrows
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SimpleBlockchainApplication.class, args);
		var params = ParamsReader.getParams(args);

		String firstKnownHost = String.valueOf(params.getOrDefault(RunParams.FIRST_KNOWN, "192.168.1.5:49471	"));
		BlockChainNode blockChainNode = context.getBean(BlockChainNode.class);
		blockChainNode.start(HostInfo.from(firstKnownHost));
	}

}
