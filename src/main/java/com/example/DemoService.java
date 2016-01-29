package com.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * Created by dev on 11/18/15.
 */
@Service
public class DemoService {

	private static Logger log = LoggerFactory.getLogger(DemoService.class);

	@Autowired
	private Tracer trace;

	private static String[] greetings = new String[] { "hallo", "hallo", "hej", "hej",
			"bonjour", "hola", "ciao", "shalom", "fáilte", "kaixo", "konnichiwa",
			"saluton", "päivää", "selamat pagi", "gut de", "olá" };

	@HystrixCommand
	public void call1() throws Exception {
		final Random random = new Random();
		int millis = random.nextInt(10);
		Thread.sleep(millis);
		trace.addTag("callable-sleep-millis", String.valueOf(millis));
		log.info("call1-sleep-millis: " + millis);
	}

	@HystrixCommand
	public void call2() throws Exception {
		final Random random = new Random();
		int millis = random.nextInt(15);
		Thread.sleep(millis);
		trace.addTag("callable-sleep-millis", String.valueOf(millis));
		log.info("call2-sleep-millis: " + millis);
	}

	@Async
	public CompletableFuture<String> callAsync() throws Exception {
		int millis = AsyncUtils.randomSleep(20, TimeUnit.MILLISECONDS);
		trace.addTag("callAsync-sleep-millis", String.valueOf(millis));
		log.info("callAsync-sleep-millis: " + millis);
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
