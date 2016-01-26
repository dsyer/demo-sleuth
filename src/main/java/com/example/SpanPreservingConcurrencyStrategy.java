package com.example;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContextHolder;

/**
 * Created by dev on 11/18/15.
 */
public class SpanPreservingConcurrencyStrategy extends HystrixConcurrencyStrategy {

  String name;
  HystrixRequestContext ctx;

  @Override
  public <T> Callable<T> wrapCallable(Callable<T> callable) {
    return new SpanPreservingCallable<T>(callable, name);
  }

  @Override
  public ThreadPoolExecutor getThreadPool(final HystrixThreadPoolKey threadPoolKey, HystrixProperty<Integer> corePoolSize, HystrixProperty<Integer> maximumPoolSize, HystrixProperty<Integer> keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
    this.name = threadPoolKey.name();
    return super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
  }

  public static class SpanPreservingCallable<T> implements Callable<T> {
    private Span currentSpan;
    private Callable<T> callable;
    private String name;

    public SpanPreservingCallable(Callable<T> callable, String name) {
      this.callable = callable;
      this.currentSpan = TraceContextHolder.getCurrentSpan();
      this.name = name;
    }

    @Override
    public T call() throws Exception {
      currentSpan.addTimelineAnnotation("Hystrix: " + name);

      try {
        // set span of the calling thread
        TraceContextHolder.setCurrentSpan(currentSpan);
        return callable.call();
      } finally {
        // reset span
        TraceContextHolder.setCurrentSpan(null);
      }
    }
  }
}