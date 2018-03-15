package com.ws.word.search;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@SpringBootApplication
@ComponentScan({"com.ws.word.search"})
public class WordSearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordSearchServiceApplication.class, args);
	}
	
	 @Bean
	  public Docket api() {
	    return new Docket(DocumentationType.SWAGGER_2).select()
	        .apis(RequestHandlerSelectors.withClassAnnotation(Api.class)).paths(PathSelectors.any()).build()
	        .pathMapping("/").apiInfo(apiInfo()).useDefaultResponseMessages(false);
	  }
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Word Search Service")
				.description("Search single or multiple word")
				.license("(C)2018 Copyright Dipanjan Nandi")
				.licenseUrl("http://cognizant.com").version("1.0")
				.build();
	}
}
