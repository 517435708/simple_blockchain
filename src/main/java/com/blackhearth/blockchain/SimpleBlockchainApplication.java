package com.blackhearth.blockchain;

import com.blackhearth.blockchain.params_reader.ParamsReader;
import com.blackhearth.blockchain.params_reader.RunParams;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class SimpleBlockchainApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleBlockchainApplication.class, args);
		Map<RunParams, Object> params = ParamsReader.getParams(args);
	}
}
