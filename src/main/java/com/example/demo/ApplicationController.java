package com.example.demo;


import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ApplicationController {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	@RequestMapping("/health")
	public ApplicationObject greeting() {
		String linkerdHealth = getLinkerdHealth().equals("pong")?"up":"down";
		log.info("linkerdHealth : " + linkerdHealth);
		
		String namerdHealth = getNamerdHealth().equals("pong")?"up":"down";
		log.info("namerdHealth : " + namerdHealth);
		
		HttpStatus linkerdTcpStatus= getLinkerdTcpHealth();
		String linkerdTcpHealth = linkerdTcpStatus.is2xxSuccessful()?"up":"down";
		log.info("linkerdTcpHealth : " + linkerdTcpHealth);
		
		HttpStatus linkerdVizStatus= getLinkerdVizHealth();
		String linkerdVizHealth = linkerdVizStatus.is2xxSuccessful()?"up":"down";
		log.info("linkerdVizHealth : " + linkerdVizHealth);
		return new ApplicationObject(linkerdHealth,namerdHealth,
				linkerdTcpHealth,linkerdVizHealth);
	}
	
	private String getLinkerdHealth(){
		final String uri = "http://localhost:9990/admin/ping";
		RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(uri, String.class);
		return result;
	}
	private String getNamerdHealth(){
		final String uri = "http://localhost:9991/admin/ping";
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(uri, String.class);
		return result;
	}
	
	private HttpStatus getLinkerdTcpHealth(){
		final String uri = "http://localhost:9992/metrics";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(uri,HttpMethod.GET,null,String.class);
		return response.getStatusCode();
	}
	
	private HttpStatus getLinkerdVizHealth(){
		final String uri = "http://localhost:3000";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(uri,HttpMethod.GET,null,String.class);
		return response.getStatusCode();
	}
}
