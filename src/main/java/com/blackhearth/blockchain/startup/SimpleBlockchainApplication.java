package com.blackhearth.blockchain.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class SimpleBlockchainApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleBlockchainApplication.class, args);
		Map<RunParams, Object> params = new RunnerImpl().getParams(args);

		String xd = (String) params.get(RunParams.FIRST_KNOWN);
		System.out.println(xd);
	}

}
