package com.blackhearth.blockchain;

import com.blackhearth.blockchain.params_reader.ParamsReader;
import com.blackhearth.blockchain.params_reader.RunParams;
import com.blackhearth.blockchain.peertopeer.InterpreterBlockChainCommunication;
import com.blackhearth.blockchain.peertopeer.BasicPeerToPeerService;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class SimpleBlockchainApplication {

	@SneakyThrows
	public static void main(String[] args) {
		SpringApplication.run(SimpleBlockchainApplication.class, args);
		Map<RunParams, Object> params = ParamsReader.getParams(args);

		BasicPeerToPeerService basicPeerToPeerService = new BasicPeerToPeerService(new InterpreterBlockChainCommunication());
		basicPeerToPeerService.start(1234);

		System.out.println(basicPeerToPeerService.getLocalNodeChainData());
	}
}
