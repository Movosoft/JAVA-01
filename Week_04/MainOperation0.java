package java0.conc0302;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * @Description
 * @auther Movo
 * @create 2021/2/3 10:41
 */
public class MainOperation {
    public static class Entity {
        private String value;

        public void set(String value) {
            this.value = value;
        }

        public String get() {
            return this.value;
        }
    }

    public static class MyCallable<T> implements Callable<T> {
        private T value;
        public MyCallable(T value) {
            this.value = value;
        }
        @Override
        public T call() throws Exception {
            return value;
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, BrokenBarrierException {
        // 01.主线程循环等待
        final Entity entity01 = new Entity();
        Thread thread01 = new Thread(() -> {
            entity01.set("Hello thread01!");
        });
        thread01.start();
        while (entity01.get() == null) {
            Thread.sleep(10);
        }
        System.out.println(entity01.get());

        // 02.join方法让主线程等待
        final Entity entity02 = new Entity();
        Thread thread02 = new Thread(() -> {
            entity02.set("Hello thread02!");
        });
        thread02.start();
        thread02.join();
        System.out.println(entity02.get());

        // 03.用实现了Callable接口的类作runner
        MyCallable<String> myCallable03 = new MyCallable<>("Hello thread03!");
        FutureTask<String> futureTask = new FutureTask<>(myCallable03);
        Thread thread03 = new Thread(futureTask);
        thread03.start();
        System.out.println("等待获取thread03结果...");
        // 会在这行阻塞等待结果
        System.out.println(futureTask.get());
        System.out.println("已获取thread03结果...");

        // 04.Executor异步提交方式
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            MyCallable<String> myCallable04 = new MyCallable<>("Hello thread04!");
            Future<String> future = executorService.submit(myCallable04);
            System.out.println("等待获取thread04结果...");
            // 会在这行阻塞等待结果
            System.out.println(future.get());
            System.out.println("已获取thread04结果...");
        } finally {
            executorService.shutdown();
        }

        // 05.子线程对象调用wait方法让主线程等待,thread05执行完会调用thread05.notifyAll()让线程继续
        final Entity entity05 = new Entity();
        Thread thread05 = new Thread(() -> {
            entity05.set("Hello thread05!");
        });
        thread05.start();
        synchronized (thread05) {
            thread05.wait();
        }
        System.out.println(entity05.get());

        // 06.CompletionStage方式
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletionStage<Entity> stage = CompletableFuture.supplyAsync(() -> {
            final Entity entity06 = new Entity();
            entity06.set("Hello thread06!");
            return entity06;
        }, executor);
        System.out.println("等待获取thread06结果...");
        // 会在这行阻塞等待结果
        stage.whenComplete((entity, error) -> {
            System.out.println(entity.get());
        });
        System.out.println("已获取thread06结果...");
        executor.shutdown();
        // 07.park
        final Entity entity07 = new Entity();
        Thread currentThread = Thread.currentThread();
        Thread thread07 = new Thread(() -> {
            entity07.set("Hello thread07!");
            LockSupport.unpark(currentThread);
        });
        thread07.start();
        LockSupport.park();
        System.out.println(entity07.get());

        // 08.CountDownLatch
        CountDownLatch latch = new CountDownLatch(1);
        final Entity entity08 = new Entity();
        Thread thread08 = new Thread(() -> {
            entity08.set("Hello thread08!");
            latch.countDown();
        });
        thread08.start();
        latch.await();
        System.out.println(entity08.get());

        // 09.CyclicBarrier
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        final Entity entity09 = new Entity();
        Thread thread09 = new Thread(() -> {
            entity09.set("Hello thread09!");
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        thread09.start();
        cyclicBarrier.await();
        System.out.println(entity09.get());

        // 10.synchronized的等待唤醒机制
        final Entity entity10 = new Entity();
        Thread thread10 = new Thread(() -> {
            entity10.set("Hello thread10!");
            synchronized(entity10) {
                entity10.notify();
            }
        });
        thread10.start();
        synchronized(entity10) {
            entity10.wait();
        }
        System.out.println(entity10.get());

        // 11.BlockingQueue
        BlockingQueue queue = new ArrayBlockingQueue(1);
        Thread thread11 = new Thread(() -> {
            try {
                queue.put("Hello thread11!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread11.start();
        System.out.println(queue.take());

        // 12.Condition
        final Entity entity12 = new Entity();
        Lock lock = new ReentrantLock();

        Condition condition = lock.newCondition();
        Thread thread12 = new Thread(() -> {
            lock.lock();
            entity12.set("Hello thread12!");
            condition.signal();
            lock.unlock();
        });
        thread12.start();
        lock.lock();
        condition.await();
        System.out.println(entity12.get());
        lock.unlock();

        // 13.BlockingDeque或SynchronousQueue
        BlockingDeque<String> deque = new LinkedBlockingDeque<>(1);
        Thread thread13 = new Thread(() -> {
            deque.add("Hello thread13!");
        });
        thread13.start();
        System.out.println(deque.take());

        // 14.semaphore
        final Entity entity14 = new Entity();
        Semaphore writeSemaphore = new Semaphore(1);
        writeSemaphore.acquire();
        Thread thread14 = new Thread(() -> {
            entity14.set("Hello thread14!");
            writeSemaphore.release();
        });
        thread14.start();
        writeSemaphore.acquire();
        System.out.println(entity14.get());
        writeSemaphore.release();

        // 15.Exchanger
        final Exchanger<String> exchanger = new Exchanger<String>();
        Thread thread15 = new Thread(() -> {
            try {
                exchanger.exchange("Hello thread15!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread15.start();
        System.out.println(exchanger.exchange(null));
    }
}
