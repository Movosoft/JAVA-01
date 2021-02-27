# 第四周作业

## 第七课 Java并发编程(2)

1. （**选做**）把示例代码，运行一遍，思考课上相关的问题。也可以做一些比较。 

   > [DaemonThread](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0301/DaemonThread.java)

   ​	当主线程结束时，守护线程会自动关闭。

   > [OnlyMain](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0301/OnlyMain.java)、[ThreadCount](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0301/ThreadCount.java)

   ​	简单的启动一个main函数，会有6个进程被创建，如下所示：

   ```
   java.lang.ThreadGroup[name=system,maxpri=10]
       Thread[Reference Handler,10,system]
       Thread[Finalizer,8,system]
       Thread[Signal Dispatcher,9,system]
       Thread[Attach Listener,5,system]
       java.lang.ThreadGroup[name=main,maxpri=10]
           Thread[main,5,main]
           Thread[Monitor Ctrl-Break,5,main]
   
   [6] Monitor Ctrl-Break(5,main)
   
   	调用线程控制中断，停止当前线程及子线程。
   	
   [5] Attach Listener(5,system)
   
   	Attach Listener线程是负责接收到外部的命令，而对该命令进行执行的并且把结果返回给发送者。通常我们会用一些命令去要求jvm给我们一些反馈信息，如：java -version、jmap、jstack等等。如果该线程在jvm启动的时候没有被初始化，则会在用户第一次执行jvm命令时启动。
   	
   [4] Signal Dispatcher(9,system)
   
   	Attach Listener线程的职责是接收外部jvm命令，当命令接收成功后，会交给Signal Dispather线程去进行分发到各个不同的模块处理命令，并且返回处理结果。Signal Dispather线程也是在第一次接收到外部jvm命令时，进行初始化工作的。
   	
   [3] Finalizer(8,system)
   
   	这个线程也是在main线程之后创建的，其优先级为8，主要用于在垃圾收集前，调用对象的finalize()方法；关于Finalizer线程的几点：
       1. 只有当开始一轮垃圾收集时，才会开始调用finalize()方法；因此并不是所有对象的finalize()方法都会被执行；
   	2. 该线程也是daemon线程，因此如果虚拟机中没有其他非daemon线程，不管该线程有没有执行完finalize()方法，JVM也会退出；
   	3. JVM在垃圾收集时会将失去引用的对象包装成Finalizer对象（Reference的实现），并放入ReferenceQueue，由Finalizer线程来处理；最后将该Finalizer对象的引用置为null，由垃圾收集器来回收；
   	4. JVM为什么要单独用一个线程来执行finalize()方法呢？如果JVM的垃圾收集线程自己来做，很有可能由于在finalize()方法中误操作导致GC线程停止或不可控，这对GC线程来说是一种灾难。
   	
   [2] Reference Handler(10,system)
   
   	JVM在创建main线程后就创建Reference Handler线程，其优先级为10，它主要用于处理引用对象本身（软引用、弱引用、虚引用）的垃圾回收问题。
   	
   [1] main(5,main)
   
   	主线程
   	
   ```

   > [RunnerMain](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0301/RunnerMain.java)、[Runner1](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0301/Runner1.java)、[Runner2](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0301/Runner2.java)

   - public static boolean interrupted()

     测试当前线程是否已被中断，此方法会清除线程的中断状态。

   - public boolean isInterrupted()

     测试此线程是否已被中断，线程的中断状态不受此方法的影响。

   > [ThreadMain](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0301/ThreadMain.java)、[ThreadA](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0301/ThreadA.java)、[ThreadB](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0301/ThreadB.java)、[ThreadC](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0301/ThreadC.java)

   ```
   ThreadA继承了Thread，Thread是实现了Runnable接口的类
   ThreadB是实现了Runnable接口的类
   ThreadC是实现了Callable接口的类
   
   interface Runnable {
   　　public abstract void run();
   }
   
   public interface Callable<V> {
   　　V call() throws Exception;
   }
   
   我们看一下Runable和Callable的区别
   	1.Callable能接受一个泛型，然后在call方法中返回一个这个类型的值。而Runnable的run方法没有返回值
   	2.Callable的call方法可以抛出异常，而Runnable的run方法不会抛出异常。
   
   FutureTask实现了RunnableFuture接口，而RunnableFuture继承了Runnable和Future，也就是说FutureTask既是Runnable，也是Future。
   ```

   > [Join](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0301/op/Join.java)

   ```
   上课时的Join.java
   thread1.join()会调用thread1的wait()方法，thread1.wait()不会使thread1进入等待状态，而是会使调用thread1.wait()的线程进入等待状态。而Object.wait会暂时释放thread1上的所有同步锁，这样会使得thread1内部可以获得thread1的锁从而可以继续执行，当thread1执行结束后，会释放内部对thread1的锁，并且thread1的this.notifyAll()会被调用，从而取消thread1所在线程的阻塞，调用thread1的线程重新获得锁继续执行。
   注意：wait方法调用时，会放弃调用wait的对象的所有同步锁，而不是调用wait的线程的所有锁，但它会使调用wait的线程进入等待状态。
   因此程序中thread1.join();和thread1.wait();的效果是等价的。
   现在的Join.java
   oo.wait(0);会释放oo的同步锁，使得thread1内部可以获得oo的锁，从而执行下去，但thread1执行完毕会调用thread1的notifyAll()，并不会通知到oo进行notify，因此主线程会一直处于等待状态，无法执行完毕。只需将oo.wait(0);改为oo.wait(n); n>0即可。
   ```

   > [Counter](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0301/sync/Counter.java)

   ```
   单线程执行sum += 1; 10000次结果为10000；
   多线程执行sum += 1; 10000次结果总是小于10000；
   是因为sum += 1并不是原子操作，为incr()方法加上同步锁或者用AtomicInteger做加法，可使多线程执行结果无误。
   ```

   > [LockMain](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0302/lock/LockMain.java)

   ```
   threadA中run方法和threadB中run()方法中存在互锁·，使两个线程运行后无休止的等待对方释放自己所需的锁，造成死锁。可在两个线程执行代码中间加上threadA.join()；暂时阻塞主线程，让threadA执行完毕后再启动threadB。
   ```

   > [LockSupportDemo](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0302/lock/LockSupportDemo.java)

   ```
   Thread对象的native实现里有一个成员代表线程的中断状态，我们可以认为它是一个bool型的变量。初始为false。
   Thread对象的native实现里有一个成员代表线程是否可以阻塞的许可permit，我们可以认为它是一个int型的变量，但它的值只能为0或1。当为1时，再累加也会维持1。初始为0。
   park/unpark实现的伪代码
   park() {
       if(permit > 0) {
           permit = 0;
           return;
       }
   
       if(中断状态 == true) {
           return;
       }
   
       阻塞当前线程;  // 将来会从这里被唤醒
   
       if(permit > 0) {
           permit = 0;
       }
   }
   unpark(Thread thread) {
       if(permit < 1) {
           permit = 1;
           if(thread处于阻塞状态)
               唤醒线程thread;
       }
   }
   interrupt()实现的伪代码
   interrupt(){
       if(中断状态 == false) {
           中断状态 = true;
       }
       unpark(this);    //注意这是Thread的成员方法，所以我们可以通过this获得Thread对象
   }
   因此interrupt()不仅会设置中断状态为true，还会去调用unpark的，所以也会把permit置为1的。
   程序中开始启动t1线程，主线程睡眠1秒，期间t1运行，静态对象获得u锁，锁住同步代码块，接着输出in t1,然后park暂停t1线程，主线程结束睡眠继续执行，启动t2线程，紧接着主线程睡眠3秒，期间t2线程运行，等待获得静态对象u的锁，3秒后，主线程结束睡眠，继续执行t1.interrupt();此代码会设置中断状态为true，还对t1进行unpark操作，从而使得t1结束park等待继续运行，从而输出“被中断了“、”继续执行”，t1线程执行结束后，释放u锁。与此同时主线程也运行了LockSupport.unpark(t2);，从而使t2的permit为1。由于u锁已释放，t2会获得u锁，锁住代码块，t2继续执行输出in t2,当执行到LockSupport.park();时检测到permit为1，直接renturn，接着输出“继续执行”，t2线程执行完毕，释放u锁。最后主线程执行完毕。
   综上代码中t1.join();和t2.join();写于不写都是一样的，因此建议删除。
   ```

   > [ExceptionDemo](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0302/threadpool/ExceptionDemo.java)

   ```
   从代码运行结果来分析，future真正执行是在future.get()的时候，而executorService.execute内执行任务的核心代码中肯定捕获了RuntimeException异常。
   ```

   > [NewCachedThreadPoolDemo](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0302/threadpool/NewCachedThreadPoolDemo.java)、[NewFixedThreadExecutorDemo](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0302/threadpool/NewFixedThreadExecutorDemo.java)、[NewScheduledThreadExecutorDemo](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0302/threadpool/NewScheduledThreadExecutorDemo.java)、[NewSingleThreadExecutorDemo](https://github.com/JavaCourse00/JavaCourseCodes/tree/main/03concurrency/0301/src/main/java/java0/conc0302/threadpool/NewSingleThreadExecutorDemo.java)

   ```
   newCachedThreadPool
   创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
   线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。
   newFixedThreadPool
   创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
   定长线程池的大小最好根据系统资源进行设置。如Runtime.getRuntime().availableProcessors()。
   newScheduledThreadPool
   创建一个定长线程池，支持定时及周期性任务执行。
   ScheduledExecutorService比Timer更安全，功能更强大。
   newSingleThreadExecutor
   创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
   结果依次输出，相当于顺序执行各个任务。
   现行大多数GUI程序都是单线程的。
   ```

   

2. （**必做**）思考有多少种方式，在main函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？ 

   ​	见代码[MainOperation.java](https://github.com/Movosoft/JAVA-01/tree/main/Week_04/MainOperation.java)。

## 第八课 Java并发编程(3)

1. （**选做**）列举常用的并发操作 API 和工具类，简单分析其使用场景和优缺点。

   - Atomic*

     提供原子操作，内部使用自重试方法，在并发压力一般的情况下效果很好，但在并发压力很大的时候，自旋导致重试过多，不建议使用。

   - ConcurrentHashMap

     解决了HashMap和LinkedHashMap的并发安全问题，使用了分段锁或CAS，用空间换回了安全。适用于有并发安全需求的场景。

   - CopyOnWriteArrayList

     解决了ArrayList和LinkedArrayList的并发安全问题，使用副本机制改进，保证了并发安全。适用于有并发安全需求的场景。

   - CompletableFuture

     一个completetableFuture就代表了一个任务。它能用Future的方法。还能做一些executorService配合futures做不了的事情。

     因为future需要等待isDone为true才能知道任务跑完了，或者就是用get方法调用的时候会出现阻塞。而使用completableFuture的使用就可以用then，when等等操作来防止以上的阻塞和轮询isDone的现象出现。completableFuture使用异步任务的操作会创建守护线程。

   - ThreadLocal

     线程本地变量，使得每一个调用的线程都能拥有一个跟其他线程隔离的变量。它可用于维护遗留系统，避免增加方法调用参数，修改一连串方法签名；进行Spring的JDBC连接以及事务管理；作为请求上下文使用。使用的时候应注意要在finally中及时进行清理，避免污染下一次的请求；避免将持有大量数据的对象放到ThreadLocal。

   - Semaphore

     Semaphore 即信号量, 是一个计数信号，即允许N个许可。

     acquire() 方法，阻塞方式获取一个许可。

     release() 方法，释放一个许可。

     如果信号量=1, 则等价于互斥锁。

     如果信号量>1, 相当于共享锁。

   - CountDownLacth

     CountDownLatch(闭锁)可以看作一个只能做减法的计数器，可以让一个或多个线程等待执行。

     场景: 主线程等待子线程把任务执行完。

   - CyclicBarrier

     CyclicBarrier(循环屏障), 可以让一组线程等待满足某个条件后同时执行。

     使用场景: 任务执行到一定阶段, 等待其塔任务对齐。

2. （**选做**）请思考：什么是并发？什么是高并发？实现高并发高可用系统需要考虑哪些因素，对于这些你是怎么理解的？ 

   > 并发

   ​	系统在运行过程中同一时间收到大于一个的请求成为并发。

   > 高并发

   ​	系统在运行过程中短时间内遇到大量操作请求成为高并发。

   > 实现高并发高可用系统需要考虑的因素

   - 响应时间：一个请求到达服务器开始至服务器发出响应之间的间隔时间。
   - 吞吐量：单位时间内程序处理的请求数量。
   - QPS：每秒响应的请求数。
   - 并发用户数：同时承载正常使用系统功能的最大用户数量。

3. （**选做**）请思考：还有哪些跟并发类似/有关的场景和问题，有哪些可以借鉴的解决办法。

   - 表单防重复提交

     可在表单加载前生成一串随机字符串传到前端input字段内，并把用一个list存如此字符串，当表单提交时检查list中是否有此字符串，如果有则处理提交内容并从list删除此字符串，若没有则忽略此请求。

   - 数据库分库分表

   - 程序恶意访问站点

     当一个ip地址多次且高频访问站点的时候，可把此ip记录在一个list中，限制此ip访问频率，如发现长时间恶意访问，可在一段时间内禁止此ip地址的一切请求。

4. （**必做**）把多线程和并发相关知识带你梳理一遍，画一个脑图，截图上传到 Github上。

   ​	xmind导出的图不全，而且太大，所以直接上传xmind格式文件。

   ​	[多线程与并发.xmind](https://github.com/Movosoft/JAVA-01/tree/main/Week_04/多线程与并发.xmind)