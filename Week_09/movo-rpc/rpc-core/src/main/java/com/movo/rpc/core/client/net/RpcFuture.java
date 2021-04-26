package com.movo.rpc.core.client.net;

import lombok.Getter;

import java.util.concurrent.*;

/**
 *
 * @author Movo
 * @create 2021/4/9 9:35
 */
public class RpcFuture<T> implements Future<T> {

    private T response;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    @Getter
    private long beginTime = System.currentTimeMillis();

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return response != null;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return response;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return countDownLatch.await(timeout, unit) ? response : null;
    }

    public void setResponse(T response) {
        this.response = response;
        countDownLatch.countDown();
    }
}
