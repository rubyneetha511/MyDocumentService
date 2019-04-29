package com.neetha.restws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.neetha.restws.property.FileProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileProperties.class
})
public class ServiceDocumentApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDocumentApplication.class, args);
	}

}
