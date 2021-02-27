package java0.conc0302;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * @Description
 * @auther Movo
 * @create 2021/2/3 10:41
 */
public class MainOperation {

    // 定义一个Entity类型用于存取数据
    public class Entity {
        private String value;

        public void set(String value) {
            this.value = value;
        }

        public String get() {
            return this.value;
        }
    }

    // 定义MyCallable类实现Callable接口，用于传递返回值。
    private class MyCallable<T> implements Callable<T> {
        private T value;
        public MyCallable(T value) {
            this.value = value;
        }
        @Override
        public T call() throws Exception {
            return value;
        }
    }

    // 封装重复操作
    private void doAction(String funcName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("-------------------------------------------------------------------");
        long start=System.currentTimeMillis();
        Method method = this.getClass().getMethod(funcName);
        String result = (String)method.invoke(this, null);
        System.out.println("异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    // 01.主线程循环等待
    public String func01() throws InterruptedException {
        final Entity entity01 = new Entity();
        Thread thread01 = new Thread(() -> {
            entity01.set("Hello thread01!");
        });
        thread01.start();
        while (entity01.get() == null) {
            Thread.sleep(10);
        }
        return entity01.get();
    }

    // 02.join方法让主线程等待
    public String func02() throws InterruptedException {
        final Entity entity02 = new Entity();
        Thread thread02 = new Thread(() -> {
            entity02.set("Hello thread02!");
        });
        thread02.start();
        thread02.join();
        return entity02.get();
    }

    // 03.用实现了Callable接口的类作runner
    public String func03() throws ExecutionException, InterruptedException {
        MyCallable<String> myCallable03 = new MyCallable<>("Hello thread03!");
        FutureTask<String> futureTask = new FutureTask<>(myCallable03);
        Thread thread03 = new Thread(futureTask);
        thread03.start();
        return futureTask.get();
    }

    // 04.Executor异步提交方式
    public String func04() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            MyCallable<String> myCallable04 = new MyCallable<>("Hello thread04!");
            Future<String> future = executorService.submit(myCallable04);
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            executorService.shutdown();
        }
    }

    // 05.子线程对象调用wait方法让主线程等待,thread05执行完会调用thread05.notifyAll()让线程继续
    public String func05() throws InterruptedException {
        final Entity entity05 = new Entity();
        Thread thread05 = new Thread(() -> {
            entity05.set("Hello thread05!");
        });
        thread05.start();
        synchronized (thread05) {
            thread05.wait();
        }
        return entity05.get();
    }

    // 06.CompletableFuture方式
    public String func06() throws ExecutionException, InterruptedException {
        CompletionStage<String> stage = CompletableFuture.supplyAsync(() -> {
            final Entity entity06 = new Entity();
            entity06.set("Hello thread06!");
            return entity06.get();
        });
        return stage.toCompletableFuture().get();
    }

    // 07.park
    public String func07() {
        final Entity entity07 = new Entity();
        Thread currentThread = Thread.currentThread();
        Thread thread07 = new Thread(() -> {
            entity07.set("Hello thread07!");
            LockSupport.unpark(currentThread);
        });
        thread07.start();
        LockSupport.park();
        return entity07.get();
    }

    // 08.CountDownLatch
    public String func08() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        final Entity entity08 = new Entity();
        Thread thread08 = new Thread(() -> {
            entity08.set("Hello thread08!");
            latch.countDown();
        });
        thread08.start();
        latch.await();
        return entity08.get();
    }

    // 09.CyclicBarrier
    public String func09() throws BrokenBarrierException, InterruptedException {
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
        return entity09.get();
    }

    // 10.synchronized的等待唤醒机制
    public String func10() throws InterruptedException {
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
        return entity10.get();
    }

    // 11.BlockingQueue或SynchronousQueue
    public String func11() throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue(1);
        Thread thread11 = new Thread(() -> {
            try {
                queue.put("Hello thread11!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread11.start();
        return queue.take();
    }

    // 12.Condition
    public String func12() throws InterruptedException {
        Lock lock = new ReentrantLock();
        try {
            final Entity entity12 = new Entity();
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
            return entity12.get();
        } finally {
            lock.unlock();
        }
    }

    // 13.BlockingDeque或SynchronousQueue
    public String func13() throws InterruptedException {
        BlockingDeque<String> deque = new LinkedBlockingDeque<>(1);
        Thread thread13 = new Thread(() -> {
            deque.add("Hello thread13!");
        });
        thread13.start();
        return deque.take();
    }

    // 14.semaphore
    public String func14() throws InterruptedException {
        final Entity entity14 = new Entity();
        Semaphore writeSemaphore = new Semaphore(1);
        writeSemaphore.acquire();
        Thread thread14 = new Thread(() -> {
            entity14.set("Hello thread14!");
            writeSemaphore.release();
        });
        thread14.start();
        writeSemaphore.acquire();
        writeSemaphore.release();
        return entity14.get();
    }

    // 15.Exchanger
    public String func15() throws InterruptedException {
        final Exchanger<String> exchanger = new Exchanger<String>();
        Thread thread15 = new Thread(() -> {
            try {
                exchanger.exchange("Hello thread15!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread15.start();
        return exchanger.exchange(null);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, BrokenBarrierException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        MainOperation operation = new MainOperation();
        // 01.主线程循环等待
        operation.doAction("func01");
        // 02.join方法让主线程等待
        operation.doAction("func02");
        // 03.用实现了Callable接口的类作runner
        operation.doAction("func03");
        // 04.Executor异步提交方式
        operation.doAction("func04");
        // 05.子线程对象调用wait方法让主线程等待,thread05执行完会调用thread05.notifyAll()让线程继续
        operation.doAction("func05");
        // 06.CompletableFuture方式
        operation.doAction("func06");
        // 07.park
        operation.doAction("func07");
        // 08.CountDownLatch
        operation.doAction("func08");
        // 09.CyclicBarrier
        operation.doAction("func09");
        // 10.synchronized的等待唤醒机制
        operation.doAction("func10");
        // 11.BlockingQueue或SynchronousQueue
        operation.doAction("func11");
        // 12.Condition
        operation.doAction("func12");
        // 13.BlockingDeque
        operation.doAction("func13");
        // 14.semaphore
        operation.doAction("func14");
        // 15.Exchanger
        operation.doAction("func15");
    }

}
