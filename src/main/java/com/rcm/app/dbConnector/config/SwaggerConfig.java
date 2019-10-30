package com.rcm.app.dbConnector.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * Swagger API config class
 * 
 * @author rcm
 *
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {
	
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.rcm.app.dbConnector.controller"))
				.paths(regex("/db.*"))
				.build()
				.apiInfo(metaInfo());
	}

	private ApiInfo metaInfo() {
		
		ApiInfo apiInfo = new ApiInfo(
				"Database Manager", 
				"Use the API to view more information about the configured DB's", 
				"1.0", 
				"Terms of Service", 
				"", 
				"", 
				"");
		
		return apiInfo;
		
	}
	
	
}
