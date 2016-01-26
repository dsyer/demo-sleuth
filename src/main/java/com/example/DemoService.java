package com.example;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Trace;
import org.springframework.cloud.sleuth.TraceContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by dev on 11/18/15.
 */
@Service
public class DemoService {

  @Autowired
  private Trace trace;

  private static String[] greetings = new String[]{
      "hallo", "hallo", "hej", "hej", "bonjour", "hola",
      "ciao", "shalom", "fáilte", "kaixo", "konnichiwa",
      "saluton", "päivää", "selamat pagi", "gut de", "olá"
  };

  @HystrixCommand
  public void call1() throws Exception {
    final Random random = new Random();
    int millis = random.nextInt(10);
    Thread.sleep(millis);
    trace.addAnnotation("callable-sleep-millis", String.valueOf(millis));
    Span currentSpan = TraceContextHolder.getCurrentSpan();
  }

  @HystrixCommand
  public void call2() throws Exception {
    final Random random = new Random();
    int millis = random.nextInt(15);
    Thread.sleep(millis);
    trace.addAnnotation("callable-sleep-millis", String.valueOf(millis));
    Span currentSpan = TraceContextHolder.getCurrentSpan();
  }

  @Async
  public CompletableFuture<String> callAsync() throws Exception {
    int millis = AsyncUtils.randomSleep(20, TimeUnit.MILLISECONDS);
    trace.addAnnotation("callAsync-sleep-millis", String.valueOf(millis));
    String result = AsyncUtils.getThreadName() + " - " + random(greetings);
    return CompletableFuture.completedFuture(result);
  }

  @SafeVarargs
  public final <T> T random(T... elements) {
    LinkedList<T> greetings = new LinkedList<>(Arrays.asList(elements));
    Collections.shuffle(greetings, ThreadLocalRandom.current());
    return greetings.getFirst();
  }
}
