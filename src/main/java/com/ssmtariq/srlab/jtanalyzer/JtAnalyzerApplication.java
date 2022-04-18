package com.ssmtariq.srlab.jtanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JtAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JtAnalyzerApplication.class, args);
		EsJavaClientTest esJavaClientTest = new EsJavaClientTest();
		try {
			esJavaClientTest.testCreateClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
