package com.newsfilter.demofilter;

import com.newsfilter.config.InternalAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(InternalAuthProperties.class)
public class NewsFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsFilterApplication.class, args);
	}
}
