package com.example;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by dev on 11/18/15.
 */
@RestController
public class DemoController {

	private static Logger log = LoggerFactory.getLogger(DemoService.class);

	@Autowired
	DemoService demoService;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/hi")
	public String hi() throws Exception {
		return hi1("hi2");
	}

	@RequestMapping("/hi/{next}")
	public String hi1(@PathVariable String next) throws Exception {
		log.info("hi1");
		demoService.call1();
		String s = this.restTemplate.getForObject("http://localhost:9090/" + next,
				String.class);
		return "hi/" + s;
	}

	@RequestMapping("/hi2")
	public String hi2() throws Exception {
		log.info("hi2");
		demoService.call2();
		String s = this.restTemplate.getForObject("http://localhost:9090/hi3",
				String.class);
		return "hi2/" + s;
	}

	@RequestMapping("/hi3")
	public CompletableFuture<String> callAsync() throws Exception {
		log.info("hi3");
		return demoService.callAsync();
	}

	@RequestMapping("/hi4")
	public String callAsyncBlocking() throws Exception {
		log.info("hi4");
		return demoService.callAsync().get();
	}

}
