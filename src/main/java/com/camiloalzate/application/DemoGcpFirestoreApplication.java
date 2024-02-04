package com.camiloalzate.application;

import com.google.cloud.spring.data.datastore.repository.config.EnableDatastoreRepositories;
import com.google.cloud.spring.data.firestore.repository.config.EnableReactiveFirestoreRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableReactiveFirestoreRepositories(basePackages = "com.camiloalzate.domain.repository")
@EnableDatastoreRepositories(basePackages = "com.camiloalzate.domain.repository")
@ComponentScan(basePackages = "com.camiloalzate")
public class DemoGcpFirestoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoGcpFirestoreApplication.class, args);
	}

}
