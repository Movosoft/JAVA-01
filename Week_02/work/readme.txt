java -Xmx2g -Xms2g -Xss256k -XX:+UseSerialGC -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps -jar ./gateway-server-0.0.1-SNAPSHOT.jar
java -Xmx2g -Xms2g -Xss256k -XX:+UseParallelGC -XX:ParallelGCThreads=8 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps -jar ./gateway-server-0.0.1-SNAPSHOT.jar
java -Xmx2g -Xms2g -Xss256k -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=8 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps -jar ./gateway-server-0.0.1-SNAPSHOT.jar
java -Xmx2g -Xms2g -Xss256k -XX:+UseG1GC -XX:ParallelGCThreads=8 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps -jar ./gateway-server-0.0.1-SNAPSHOT.jar


java -Xmx2g -Xms2g -Xss256k -XX:+UseSerialGC -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
java -Xmx2g -Xms2g -Xss256k -XX:+UseParallelGC -XX:ParallelGCThreads=8 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
java -Xmx2g -Xms2g -Xss256k -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=8 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
java -Xmx2g -Xms2g -Xss256k -XX:+UseG1GC -XX:ConcGCThreads=2 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

java -Xmx2g -Xms2g -Xss256k -XX:+UseG1GC -XX:G1HeapRegionSize=8M -XX:ConcGCThreads=2 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

sb -u http://localhost:8088/api/hello -n 100000 -c 50

sb -u http://10.0.3.15:8000/user/info -n 100000 -c 50

sb -u http://localhost:8803 -n 600000 -c 8

-XX:MetaspaceSize=128M

通过GC日志可以看到，old区离最大配置还很远，Metaspace区并没有真正释放空间，所以怀疑是Metaspace区不够用了。

以前只认为，Metaspace区是保存在本地内存中，是没有上限的，经查阅资料才发现，原来JDK8中，XX:MaxMetaspaceSize确实是没有上限的，最大容量与机器的内存有关；但是XX:MetaspaceSize是有一个默认值的：21M。问题就出在这里。

元数据区扩容前会进行FullGC