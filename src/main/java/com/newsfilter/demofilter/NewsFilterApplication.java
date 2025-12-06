package com.newsfilter.demofilter;

import com.newsfilter.demofilter.config.InternalAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableConfigurationProperties(InternalAuthProperties.class)
@EnableJpaRepositories(basePackages = "com.newsfilter.demofilter.repository.jpa")
@EnableMongoRepositories(basePackages = "com.newsfilter.demofilter.repository.mongo")
public class NewsFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsFilterApplication.class, args);
	}
}
