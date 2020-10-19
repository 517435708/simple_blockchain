package com.blackhearth.blockchain;

import com.blackhearth.blockchain.p2p.TcpClient;
import com.blackhearth.blockchain.startup.ParamsReader;
import com.blackhearth.blockchain.startup.RunParams;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class SimpleBlockchainApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleBlockchainApplication.class, args);
		Map<RunParams, Object> params = ParamsReader.getParams(args);

		new TcpClient().start();
	}
}
