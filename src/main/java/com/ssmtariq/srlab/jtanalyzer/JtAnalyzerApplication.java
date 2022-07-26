package com.ssmtariq.srlab.jtanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JtAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JtAnalyzerApplication.class, args);
		try {
			ElasticSearchJClient elasticSearchJClient = new ElasticSearchJClient();
			elasticSearchJClient.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
