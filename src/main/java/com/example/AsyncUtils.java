package com.example;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by dev on 11/23/15.
 */
public class AsyncUtils {
	public static int randomSleep(int duration, TimeUnit timeUnit) {
		int sleepTime = ThreadLocalRandom.current().nextInt(duration);
		try {
			timeUnit.sleep(sleepTime);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Interrupted");
		}
		return sleepTime;
	}

	public static String getThreadName() {
		return Thread.currentThread().getName();
	}
}