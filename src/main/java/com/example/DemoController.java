package com.example;

import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by dev on 11/18/15.
 */
@RestController
public class DemoController {

  @Autowired
  DemoService demoService;

  @Autowired
  private RestTemplate restTemplate;

  @RequestMapping("/hi")
  public String hi1() throws Exception {
    demoService.call1();
    String s = this.restTemplate.getForObject("http://localhost:9090/hi2", String.class);
    return "hi/" + s;
  }

  @RequestMapping("/hi2")
  public String hi2() throws Exception {
    demoService.call2();
    String s = this.restTemplate.getForObject("http://localhost:9090/hi3", String.class);
    return "hi2/" + s;
  }

  @RequestMapping("/hi3")
  public CompletableFuture<String> callAsync() throws Exception {
    return demoService.callAsync();
  }
}
