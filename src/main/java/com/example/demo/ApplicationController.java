package com.example.demo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ApplicationController {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	RestTemplate restTemplate = new RestTemplate();
	@Value("${spring.datasource.docker_ip}")
	private String host;

	@RequestMapping("/health")
	public ApplicationObject getHealth() {
		String linkerdHealth = getLinkerdHealth();
		log.info("linkerdHealth : " + linkerdHealth);

		String namerdHealth = getNamerdHealth();
		log.info("namerdHealth : " + namerdHealth);

		String linkerdTcpHealth= getLinkerdTcpHealth();
		log.info("linkerdTcpHealth : " + linkerdTcpHealth);

		String linkerdVizHealth= getLinkerdVizHealth();
		log.info("linkerdVizHealth : " + linkerdVizHealth);
		return new ApplicationObject(linkerdHealth,namerdHealth,
				linkerdTcpHealth,linkerdVizHealth);
	}

	@RequestMapping("/shift/{value}")
	public HttpStatus update(@PathVariable String value) {
		log.info("value =" + value);
		int redis2_data = Integer.parseInt(value);
		int redis1_data = 100-redis2_data;
		return updateData(redis1_data,redis2_data);
	}

	private HttpStatus updateData(int r1, int r2){
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/dtab");
		String url = "http://"+host+":4180/api/1/dtabs/default";
		String requestBody = "/svc => /#/io.l5d.fs; /svc/redis=> "
				+r1+"*/svc/redis1 & "+r2+"*/svc/redis2;";
		HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
		ResponseEntity<String> response = 
				restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
		log.info(response.getBody());
		log.info(response.getHeaders().toString());
		log.info(response.toString());
		return response.getStatusCode();
	}

	private String getHealth(String uri){
		if(uri.contains("9992") || uri.contains("3000")){
			try{
				ResponseEntity<String> response = 
						restTemplate.exchange(uri,HttpMethod.GET,null,String.class);
				log.info(response.toString());
				return  response.getStatusCode().is2xxSuccessful() ?"up":"down";
			}catch(Exception e){
				return "down";
			}
		}else{
			try{
				ResponseEntity<String> response = 
						restTemplate.exchange(uri, HttpMethod.GET,null,String.class);
				log.info(response.toString());
				if (!response.getStatusCode().is2xxSuccessful()){
					return "down";
				}else{
					return restTemplate.getForObject(uri, String.class)
							.equals("pong")?"up":"down";
				}
			}catch(Exception e){
				return "down";
			}
		}
	}

	private String getLinkerdHealth(){
		final String uri = "http://"+host+":9990/admin/ping";
		return getHealth(uri);
	}

	private String getNamerdHealth(){
		final String uri = "http://"+host+":9991/admin/ping";
		return getHealth(uri);
	}

	private String getLinkerdTcpHealth(){
		final String uri = "http://"+host+":9992/metrics";
		return getHealth(uri);
	}

	private String getLinkerdVizHealth(){
		final String uri = "http://"+host+":3000";
		return getHealth(uri);
	}
}
