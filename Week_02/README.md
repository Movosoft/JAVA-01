# 第二周作业
## 第三课 JVM 核心技术--调优分析与面试经验
1. (**选做**)使用 GCLogAnalysis.java 自己演练一遍串行/并行/CMS/G1的案例。
   > Serial GC
   ```
   执行java -Xmx2g -Xms2g -Xss256k -XX:+UseSerialGC -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
   正在执行...
   2021-01-15T14:48:23.576+0800: [GC (Allocation Failure) 2021-01-15T14:48:23.580+0800: [DefNew: 559232K->69888K(629120K), 0.0666345 secs] 559232K->161307K(2027264K), 0.0708825 secs] [Times: user=0.01 sys=0.05, real=0.07 secs]
   2021-01-15T14:48:23.708+0800: [GC (Allocation Failure) 2021-01-15T14:48:23.708+0800: [DefNew: 629120K->69887K(629120K), 0.0786665 secs] 720539K->282603K(2027264K), 0.0792952 secs] [Times: user=0.05 sys=0.02, real=0.08 secs]
   2021-01-15T14:48:23.837+0800: [GC (Allocation Failure) 2021-01-15T14:48:23.837+0800: [DefNew: 629119K->69887K(629120K), 0.0578704 secs] 841835K->405673K(2027264K), 0.0587669 secs] [Times: user=0.02 sys=0.03, real=0.06 secs]
   2021-01-15T14:48:23.947+0800: [GC (Allocation Failure) 2021-01-15T14:48:23.947+0800: [DefNew: 628994K->69887K(629120K), 0.0631332 secs] 964779K->540136K(2027264K), 0.0635927 secs] [Times: user=0.00 sys=0.06, real=0.06 secs]
   2021-01-15T14:48:24.059+0800: [GC (Allocation Failure) 2021-01-15T14:48:24.060+0800: [DefNew: 629119K->69887K(629120K), 0.0652431 secs] 1099368K->677859K(2027264K), 0.0658721 secs] [Times: user=0.03 sys=0.03, real=0.07 secs]
   2021-01-15T14:48:24.178+0800: [GC (Allocation Failure) 2021-01-15T14:48:24.178+0800: [DefNew: 629119K->69887K(629120K), 0.0565601 secs] 1237091K->803612K(2027264K), 0.0571666 secs] [Times: user=0.06 sys=0.00, real=0.06 secs]
   2021-01-15T14:48:24.287+0800: [GC (Allocation Failure) 2021-01-15T14:48:24.288+0800: [DefNew: 629119K->69887K(629120K), 0.0562064 secs] 1362844K->928693K(2027264K), 0.0568102 secs] [Times: user=0.05 sys=0.00, real=0.06 secs]
   执行结束!共生成对象次数:15357
   Heap
    def new generation   total 629120K, used 199248K [0x0000000080000000, 0x00000000aaaa0000, 0x00000000aaaa0000)
     eden space 559232K,  23% used [0x0000000080000000, 0x0000000087e54110, 0x00000000a2220000)
     from space 69888K,  99% used [0x00000000a6660000, 0x00000000aaa9fff8, 0x00000000aaaa0000)
     to   space 69888K,   0% used [0x00000000a2220000, 0x00000000a2220000, 0x00000000a6660000)
    tenured generation   total 1398144K, used 858805K [0x00000000aaaa0000, 0x0000000100000000, 0x0000000100000000)
      the space 1398144K,  61% used [0x00000000aaaa0000, 0x00000000df14d4a8, 0x00000000df14d600, 0x0000000100000000)
    Metaspace       used 2757K, capacity 4486K, committed 4864K, reserved 1056768K
     class space    used 306K, capacity 386K, committed 512K, reserved 1048576K
   
   ```
   >> 分析
   - 共生成对象15357次。
   - 共发生GC7次，无Full GC。平均GC暂停时长65.7ms,最小GC暂停时长60.0ms，最大GC暂停时长80.0ms,GC暂定总时长460ms。
   > Parallel GC
   ```
   执行java -Xmx2g -Xms2g -Xss256k -XX:+UseParallelGC -XX:ParallelGCThreads=8 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
   正在执行...
   2021-01-15T15:01:57.479+0800: [GC (Allocation Failure) [PSYoungGen: 524800K->87039K(611840K)] 524800K->134745K(2010112K), 0.0194873 secs] [Times: user=0.06 sys=0.06, real=0.02 secs]
   2021-01-15T15:01:57.548+0800: [GC (Allocation Failure) [PSYoungGen: 611839K->87027K(611840K)] 659545K->256304K(2010112K), 0.0258730 secs] [Times: user=0.13 sys=0.13, real=0.03 secs]
   2021-01-15T15:01:57.620+0800: [GC (Allocation Failure) [PSYoungGen: 611827K->87038K(611840K)] 781104K->364250K(2010112K), 0.0224825 secs] [Times: user=0.03 sys=0.09, real=0.02 secs]
   2021-01-15T15:01:57.690+0800: [GC (Allocation Failure) [PSYoungGen: 611838K->87026K(611840K)] 889050K->485006K(2010112K), 0.0230108 secs] [Times: user=0.14 sys=0.11, real=0.02 secs]
   2021-01-15T15:01:57.763+0800: [GC (Allocation Failure) [PSYoungGen: 611826K->87038K(611840K)] 1009806K->600571K(2010112K), 0.0248463 secs] [Times: user=0.14 sys=0.00, real=0.03 secs]
   2021-01-15T15:01:57.834+0800: [GC (Allocation Failure) [PSYoungGen: 611838K->87038K(611840K)] 1125371K->721320K(2010112K), 0.0289738 secs] [Times: user=0.14 sys=0.09, real=0.03 secs]
   2021-01-15T15:01:57.911+0800: [GC (Allocation Failure) [PSYoungGen: 611838K->87029K(611840K)] 1246120K->835417K(2010112K), 0.0352262 secs] [Times: user=0.19 sys=0.17, real=0.03 secs]
   2021-01-15T15:01:57.994+0800: [GC (Allocation Failure) [PSYoungGen: 611829K->87031K(611840K)] 1360217K->950046K(2010112K), 0.0285847 secs] [Times: user=0.03 sys=0.08, real=0.03 secs]
   2021-01-15T15:01:58.074+0800: [GC (Allocation Failure) [PSYoungGen: 611831K->87037K(611840K)] 1474846K->1062505K(2010112K), 0.0294796 secs] [Times: user=0.03 sys=0.14, real=0.03 secs]
   2021-01-15T15:01:58.151+0800: [GC (Allocation Failure) [PSYoungGen: 611837K->87037K(611840K)] 1587305K->1170886K(2010112K), 0.0289221 secs] [Times: user=0.05 sys=0.16, real=0.03 secs]
   2021-01-15T15:01:58.227+0800: [GC (Allocation Failure) [PSYoungGen: 611541K->87023K(611840K)] 1695390K->1294535K(2010112K), 0.0316171 secs] [Times: user=0.03 sys=0.11, real=0.03 secs]
   执行结束!共生成对象次数:22301
   Heap
    PSYoungGen      total 611840K, used 247924K [0x00000000d5580000, 0x0000000100000000, 0x0000000100000000)
     eden space 524800K, 30% used [0x00000000d5580000,0x00000000df2a1618,0x00000000f5600000)
     from space 87040K, 99% used [0x00000000f5600000,0x00000000faafbd90,0x00000000fab00000)
     to   space 87040K, 0% used [0x00000000fab00000,0x00000000fab00000,0x0000000100000000)
    ParOldGen       total 1398272K, used 1207511K [0x0000000080000000, 0x00000000d5580000, 0x00000000d5580000)
     object space 1398272K, 86% used [0x0000000080000000,0x00000000c9b35f90,0x00000000d5580000)
    Metaspace       used 2757K, capacity 4486K, committed 4864K, reserved 1056768K
     class space    used 306K, capacity 386K, committed 512K, reserved 1048576K
   
   ```
   >> 分析
   - 共生成对象22301次。
   - 共发生GC11次，无Full GC。平均GC暂停时长27.3ms，最小GC暂停时长20.0ms，最大GC暂停时长30.0ms，GC暂停总时长300ms。
   > CMS GC
   ```
   执行java -Xmx2g -Xms2g -Xss256k -XX:+UseConcMarkSweepGC -XX:ParallelGCThreads=8 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
   正在执行...
   2021-01-15T15:08:59.949+0800: [GC (Allocation Failure) 2021-01-15T15:08:59.952+0800: [ParNew: 545344K->68095K(613440K), 0.0194999 secs] 545344K->149612K(2029056K), 0.0226075 secs] [Times: user=0.06 sys=0.06, real=0.02 secs]
   2021-01-15T15:09:00.026+0800: [GC (Allocation Failure) 2021-01-15T15:09:00.028+0800: [ParNew: 613439K->68096K(613440K), 0.0253910 secs] 694956K->264261K(2029056K), 0.0267076 secs] [Times: user=0.05 sys=0.08, real=0.03 secs]
   2021-01-15T15:09:00.101+0800: [GC (Allocation Failure) 2021-01-15T15:09:00.101+0800: [ParNew: 613440K->68096K(613440K), 0.0582604 secs] 809605K->392451K(2029056K), 0.0590567 secs] [Times: user=0.38 sys=0.00, real=0.06 secs]
   2021-01-15T15:09:00.208+0800: [GC (Allocation Failure) 2021-01-15T15:09:00.209+0800: [ParNew: 613440K->68094K(613440K), 0.0575972 secs] 937795K->516500K(2029056K), 0.0581241 secs] [Times: user=0.48 sys=0.00, real=0.06 secs]
   2021-01-15T15:09:00.319+0800: [GC (Allocation Failure) 2021-01-15T15:09:00.319+0800: [ParNew: 613438K->68096K(613440K), 0.0557584 secs] 1061844K->637885K(2029056K), 0.0561663 secs] [Times: user=0.50 sys=0.00, real=0.06 secs]
   2021-01-15T15:09:00.425+0800: [GC (Allocation Failure) 2021-01-15T15:09:00.425+0800: [ParNew: 613183K->68095K(613440K), 0.0543810 secs] 1182973K->754327K(2029056K), 0.0547762 secs] [Times: user=0.45 sys=0.05, real=0.05 secs]
   2021-01-15T15:09:00.527+0800: [GC (Allocation Failure) 2021-01-15T15:09:00.527+0800: [ParNew: 613439K->68094K(613440K), 0.0542444 secs] 1299671K->871379K(2029056K), 0.0546468 secs] [Times: user=0.30 sys=0.05, real=0.06 secs]
   2021-01-15T15:09:00.582+0800: [GC (CMS Initial Mark) [1 CMS-initial-mark: 803285K(1415616K)] 882258K(2029056K), 0.0002577 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
   2021-01-15T15:09:00.582+0800: [CMS-concurrent-mark-start]
   2021-01-15T15:09:00.586+0800: [CMS-concurrent-mark: 0.004/0.004 secs] [Times: user=0.05 sys=0.00, real=0.00 secs]
   2021-01-15T15:09:00.586+0800: [CMS-concurrent-preclean-start]
   2021-01-15T15:09:00.588+0800: [CMS-concurrent-preclean: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
   2021-01-15T15:09:00.589+0800: [CMS-concurrent-abortable-preclean-start]
   2021-01-15T15:09:00.633+0800: [GC (Allocation Failure) 2021-01-15T15:09:00.633+0800: [ParNew: 613438K->68094K(613440K), 0.0564996 secs] 1416723K->995066K(2029056K), 0.0569940 secs] [Times: user=0.34 sys=0.02, real=0.06 secs]
   执行结束!共生成对象次数:18290
   Heap
    par new generation   total 613440K, used 584338K [0x0000000080000000, 0x00000000a9990000, 0x00000000a9990000)
     eden space 545344K,  94% used [0x0000000080000000, 0x000000009f8251c8, 0x00000000a1490000)
     from space 68096K,  99% used [0x00000000a1490000, 0x00000000a570f948, 0x00000000a5710000)
     to   space 68096K,   0% used [0x00000000a5710000, 0x00000000a5710000, 0x00000000a9990000)
    concurrent mark-sweep generation total 1415616K, used 926971K [0x00000000a9990000, 0x0000000100000000, 0x0000000100000000)
    Metaspace       used 2757K, capacity 4486K, committed 4864K, reserved 1056768K
     class space    used 306K, capacity 386K, committed 512K, reserved 1048576K
   
   ```
   >> 分析
   - 共生成对象18290次。
   - 共发生GC8次，无Full GC。平均GC暂停时长50.0ms，最小GC暂停时长20.0ms，最大GC暂停时长60.0ms，GC暂停总时长400ms。
   > G1 GC
   ```
   执行java -Xmx2g -Xms2g -Xss256k -XX:+UseG1GC -XX:ConcGCThreads=2 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
   正在执行...
   2021-01-15T15:17:16.975+0800: [GC pause (G1 Evacuation Pause) (young), 0.0067699 secs]
      [Parallel Time: 4.6 ms, GC Workers: 8]
         [GC Worker Start (ms): Min: 182.8, Avg: 183.0, Max: 183.4, Diff: 0.5]
         [Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 0.9]
         [Update RS (ms): Min: 0.1, Avg: 0.3, Max: 0.4, Diff: 0.3, Sum: 2.6]
            [Processed Buffers: Min: 2, Avg: 6.1, Max: 8, Diff: 6, Sum: 49]
         [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Object Copy (ms): Min: 3.9, Avg: 3.9, Max: 4.0, Diff: 0.1, Sum: 31.3]
         [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 0.4]
            [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 8]
         [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.2]
         [GC Worker Total (ms): Min: 4.0, Avg: 4.4, Max: 4.6, Diff: 0.5, Sum: 35.5]
         [GC Worker End (ms): Min: 187.4, Avg: 187.4, Max: 187.4, Diff: 0.0]
      [Code Root Fixup: 0.0 ms]
      [Code Root Purge: 0.0 ms]
      [Clear CT: 0.6 ms]
      [Other: 1.5 ms]
         [Choose CSet: 0.0 ms]
         [Ref Proc: 0.1 ms]
         [Ref Enq: 0.0 ms]
         [Redirty Cards: 0.5 ms]
         [Humongous Register: 0.0 ms]
         [Humongous Reclaim: 0.0 ms]
         [Free CSet: 0.0 ms]
      [Eden: 102.0M(102.0M)->0.0B(89.0M) Survivors: 0.0B->13.0M Heap: 126.9M(2048.0M)->47.8M(2048.0M)]
    [Times: user=0.00 sys=0.00, real=0.01 secs]
   ......
   2021-01-15T15:17:17.881+0800: [GC pause (G1 Evacuation Pause) (mixed), 0.0057163 secs]
      [Parallel Time: 4.3 ms, GC Workers: 8]
         [GC Worker Start (ms): Min: 1088.5, Avg: 1088.5, Max: 1088.6, Diff: 0.1]
         [Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 0.7]
         [Update RS (ms): Min: 0.2, Avg: 0.3, Max: 0.6, Diff: 0.4, Sum: 2.2]
            [Processed Buffers: Min: 0, Avg: 3.8, Max: 5, Diff: 5, Sum: 30]
         [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
         [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Object Copy (ms): Min: 3.3, Avg: 3.7, Max: 3.8, Diff: 0.5, Sum: 29.6]
         [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 0.5]
            [Termination Attempts: Min: 1, Avg: 2.0, Max: 4, Diff: 3, Sum: 16]
         [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
         [GC Worker Total (ms): Min: 4.1, Avg: 4.1, Max: 4.2, Diff: 0.1, Sum: 33.1]
         [GC Worker End (ms): Min: 1092.7, Avg: 1092.7, Max: 1092.7, Diff: 0.0]
      [Code Root Fixup: 0.0 ms]
      [Code Root Purge: 0.0 ms]
      [Clear CT: 0.4 ms]
      [Other: 1.0 ms]
         [Choose CSet: 0.0 ms]
         [Ref Proc: 0.1 ms]
         [Ref Enq: 0.0 ms]
         [Redirty Cards: 0.2 ms]
         [Humongous Register: 0.1 ms]
         [Humongous Reclaim: 0.1 ms]
         [Free CSet: 0.1 ms]
      [Eden: 89.0M(89.0M)->0.0B(506.0M) Survivors: 13.0M->13.0M Heap: 681.3M(2048.0M)->574.6M(2048.0M)]
    [Times: user=0.11 sys=0.00, real=0.02 secs]
   执行结束!共生成对象次数:15190
   Heap
    garbage-first heap   total 2097152K, used 772089K [0x0000000080000000, 0x0000000080104000, 0x0000000100000000)
     region size 1024K, 154 young (157696K), 13 survivors (13312K)
    Metaspace       used 2757K, capacity 4486K, committed 4864K, reserved 1056768K
     class space    used 306K, capacity 386K, committed 512K, reserved 1048576K

   ```
   >> 分析
   - 共生成对象15190次。
   - 共发生GC19次，无Full GC。平均GC暂停时长17.9ms，最小GC暂停时长0ms，最大GC暂停时长30.0ms，GC暂停总时长340ms。
   >>> 生成对象次的数不是很理想，由于程序会生成大对象，把G1HeapRegionSize的值由默认的1M改为8M，再次执行程序,对象生成次数提升28%。
   ```
   java -Xmx2g -Xms2g -Xss256k -XX:+UseG1GC -XX:G1HeapRegionSize=8M -XX:ConcGCThreads=2 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
   正在执行...
   2021-01-15T15:27:22.276+0800: [GC pause (G1 Evacuation Pause) (young), 0.0064286 secs]
      [Parallel Time: 4.1 ms, GC Workers: 8]
         [GC Worker Start (ms): Min: 148.8, Avg: 148.8, Max: 148.8, Diff: 0.1]
         [Ext Root Scanning (ms): Min: 0.1, Avg: 0.1, Max: 0.3, Diff: 0.2, Sum: 1.1]
         [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
            [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]
         [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Object Copy (ms): Min: 3.5, Avg: 3.7, Max: 3.8, Diff: 0.3, Sum: 29.6]
         [Termination (ms): Min: 0.0, Avg: 0.2, Max: 0.4, Diff: 0.4, Sum: 1.2]
            [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 8]
         [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.2]
         [GC Worker Total (ms): Min: 4.0, Avg: 4.0, Max: 4.0, Diff: 0.1, Sum: 32.1]
         [GC Worker End (ms): Min: 152.8, Avg: 152.8, Max: 152.8, Diff: 0.0]
      [Code Root Fixup: 0.0 ms]
      [Code Root Purge: 0.0 ms]
      [Clear CT: 0.5 ms]
      [Other: 1.8 ms]
         [Choose CSet: 0.0 ms]
         [Ref Proc: 0.1 ms]
         [Ref Enq: 0.0 ms]
         [Redirty Cards: 0.6 ms]
         [Humongous Register: 0.0 ms]
         [Humongous Reclaim: 0.0 ms]
         [Free CSet: 0.0 ms]
      [Eden: 96.0M(96.0M)->0.0B(80.0M) Survivors: 0.0B->16.0M Heap: 96.0M(2048.0M)->31.4M(2048.0M)]
    [Times: user=0.00 sys=0.00, real=0.01 secs]
   ......
   2021-01-15T15:27:23.145+0800: [GC pause (G1 Evacuation Pause) (young), 0.0240697 secs]
      [Parallel Time: 23.0 ms, GC Workers: 8]
         [GC Worker Start (ms): Min: 1017.2, Avg: 1017.3, Max: 1017.4, Diff: 0.2]
         [Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 0.5]
         [Update RS (ms): Min: 0.0, Avg: 0.1, Max: 0.7, Diff: 0.6, Sum: 1.1]
            [Processed Buffers: Min: 1, Avg: 1.1, Max: 2, Diff: 1, Sum: 9]
         [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Object Copy (ms): Min: 21.7, Avg: 22.4, Max: 22.7, Diff: 1.0, Sum: 179.1]
         [Termination (ms): Min: 0.0, Avg: 0.2, Max: 0.4, Diff: 0.4, Sum: 1.8]
            [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 8]
         [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
         [GC Worker Total (ms): Min: 22.8, Avg: 22.8, Max: 22.9, Diff: 0.2, Sum: 182.6]
         [GC Worker End (ms): Min: 1040.1, Avg: 1040.1, Max: 1040.1, Diff: 0.0]
      [Code Root Fixup: 0.0 ms]
      [Code Root Purge: 0.0 ms]
      [Clear CT: 0.2 ms]
      [Other: 0.9 ms]
         [Choose CSet: 0.0 ms]
         [Ref Proc: 0.1 ms]
         [Ref Enq: 0.0 ms]
         [Redirty Cards: 0.1 ms]
         [Humongous Register: 0.0 ms]
         [Humongous Reclaim: 0.0 ms]
         [Free CSet: 0.1 ms]
      [Eden: 608.0M(608.0M)->0.0B(544.0M) Survivors: 104.0M->96.0M Heap: 1612.1M(2048.0M)->1113.3M(2048.0M)]
    [Times: user=0.09 sys=0.09, real=0.03 secs]
   执行结束!共生成对象次数:19478
   Heap
    garbage-first heap   total 2097152K, used 1672540K [0x0000000080000000, 0x0000000080800800, 0x0000000100000000)
     region size 8192K, 78 young (638976K), 12 survivors (98304K)
    Metaspace       used 2757K, capacity 4486K, committed 4864K, reserved 1056768K
     class space    used 306K, capacity 386K, committed 512K, reserved 1048576K
   
   ```
   >> 分析
   - 共生成对象19478次。
   - 共发生GC16次，无Full GC。平均GC暂停时长22.5ms，最小GC暂停时长10ms，最大GC暂停时长40.0ms，GC暂停总时长360ms。
2. (**选做**)使用压测工具（wrk或sb），演练gateway-server-0.0.1-SNAPSHOT.jar示例。
    > 执行java -Xmx2g -Xms2g -Xss256k -XX:+UseG1GC -XX:ParallelGCThreads=8 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps -jar ./gateway-server-0.0.1-SNAPSHOT.jar运行示例
    >> 执行sb -u http://localhost:8088/api/hello -n 100000 -c 50进行压力测试
    ```
       99996   (RPS: 2514.2)
       ---------------Finished!----------------
       Finished at 2021/1/15 16:23:38 (took 00:00:39.9733695)
       Status 200:    100000
       
       RPS: 2450.8 (requests/second)
       Max: 411ms
       Min: 0ms
       Avg: 3.9ms
       
         50%   below 0ms
         60%   below 0ms
         70%   below 2ms
         80%   below 6ms
         90%   below 13ms
         95%   below 22ms
         98%   below 32ms
         99%   below 38ms
         99.9%   below 66ms
    ```
   - 总访问量100000次，并发50，RPS2450.8。
   - 共发生了10次GC，无Full GC。平均GC暂停时长12ms,最小GC暂停时长0ms，最大GC暂停时长30.0ms,GC暂定总时长120ms。
3. (**选做**)如果自己本地有可以运行的项目，可以按照2的方式进行演练。
    > JVM参数 -Xmx2g -Xms2g -Xss256k -XX:+UseG1GC -XX:ParallelGCThreads=8 -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps
    >> 执行sb -u http://10.0.3.15:8000/user/info -n 100000 -c 50进行压力测试
    ```
    Starting at 2021/1/18 13:47:37
    [Press C to stop the test]
    99981   (RPS: 2008.5)
    ---------------Finished!----------------
    Finished at 2021/1/18 13:48:27 (took 00:00:49.9597748)
    Status 200:    100000
    
    RPS: 1966.7 (requests/second)
    Max: 109ms
    Min: 0ms
    Avg: 5.5ms
    
      50%   below 1ms
      60%   below 3ms
      70%   below 6ms
      80%   below 10ms
      90%   below 18ms
      95%   below 26ms
      98%   below 34ms
      99%   below 39ms
      99.9%   below 51ms
   
    ```
   - 总访问量100000次，并发50，RPS1966.7。
   - 共发生了3次GC，无Full GC。平均GC暂停时长23.3ms,最小GC暂停时长20ms，最大GC暂停时长30.0ms,GC暂定总时长70ms。
4. (**必做**)根据上述自己对于1和2的演示，写一段对于不同GC和堆内存的总结，提交到 github。
    > 串行 GC（Serial GC）/ParNewGC
    - -XX:+UseSerialGC 配置串行 GC
    - YG使用mark-copy（标记-复制）算法，OG使用mark-sweep- compact（标记-清除-整理）算法。
    - 适用于单核服务器，单线程GC，CPU利用率高，暂停时间长。
    - 回收效率比较稳定，不需要通过调整复杂的参数来提升回收效率。
    > 并行 GC（Parallel GC）
    - -XX:+UseParallelGC -XX:+UseParallelOldGC配置并行GC
    - YG使用mark-copy（标记-复制）算法，OG使用mark-sweep- compact（标记-清除-整理）算法。
    - -XX:ParallelGCThreads=N 来指定 GC 线程数， 其默认值为 CPU 核心数。
    - 适用于多核服务器，在GC期间，所有CPU内核都在并行清理垃圾，可使总暂停时间更短。
    - 可以达到较高吞吐量。
    > CMS GC（Mostly Concurrent Mark and Sweep Garbage Collector）
    - -XX:+UseConcMarkSweepGC
    - YG使用mark-copy（标记-复制）算法，OG使用mark-sweep- compact（标记-清除）算法。
    - 为减少GC在老年代垃圾收集时的卡顿时间，CMS GC不对老年代进行整理，使用空闲列表（free-lists）来管理内存空间的回收，OG的大部分工作和应用线程一起并发执行。
    - 默认情况下，CMS使用的并发线程数等于CPU核心数的 1/4。
    - 适用于多核服务器，可以减低系统延迟。
    - CMS GC处理步骤
        ```
        阶段 1: Initial Mark（初始标记）,标记根对象及其直接引用对象和年轻代中所有存活对象，伴随STW。
        阶段 2: Concurrent Mark（并发标记），编辑老年代所有存活对象。
        阶段 3: Concurrent Preclean（并发预清理），修正阶段2的脏数据。
        阶段 4: Final Remark（最终标记），完成老年代中所有存活对象的标记，伴随STW，通常CMS会尝试在年轻代尽可能空的情况下执行此阶段，以避免连续STW。
        阶段 5: Concurrent Sweep（并发清除），JVM在此阶段删除不再使用的对象，并回收他们占用的内存空间。
        阶段 6: Concurrent Reset（并发重置），重置CMS算法相关的内部数据，为下一次GC循环做准备。
        ```
    > G1 GC(Garbage-First GC)
    - -XX:+UseG1GC
    - 将堆划分为多个小块堆区，通常是2048个，每个小块可能一会被定义成Eden区，一会被指定为urvivor区或者Old区。
    - 每次GC暂停都会收集所有年轻代的内存块，但一般只包含部分老年代的内存块。
    - G1 GC标记步骤
        ```
        阶段 1: Initial Mark（初始标记）
            在此阶段标记GC roots,一般是附加在某次常规的年轻代GC中顺带着执行。
        阶段 2: Root Region Scan（Root区扫描）
            根据初始标记阶段确定的GC根元素，扫描这些元素所在region，获取对老年代的引用，并标记被引用的对象。该阶段与应用线程并发执行，也就是说没有STW停顿，必须在下一次年轻代GC开始之前完成。
        阶段 3: Concurrent Mark（并发标记）
            遍历整个堆，查找所有可达的存活对象。此阶段与应用线程并发执行，也允许被年轻代GC打断。
        阶段 4: Remark（再次标记）
             此阶段有一次STW暂停，以完成标记周期。 G1会清空SATB缓冲区，跟踪未访问到的存活对象，并进行引用处理。
        阶段 5: Cleanup（清理）
             这是最后的子阶段，G1在执行统计和清理RSet时会有一次STW停顿。 在统计过程中，会把完全空闲的region标记出来，也会标记出适合于进行混合模式GC的候选region。 清理阶段有一部分是并发执行的，比如在重置空闲region并将其加入空闲列表时。
        ```
    - G1 GC处理步骤
        ```
        1、年轻代模式转移暂停（Evacuation Pause）
          G1将绝大部分的内存分配请求打到eden区。
          在年轻代模式的垃圾收集过程中，G1会收集eden区和前一次GC使用的存活区。
          并将存活对象拷贝/转移到一些新的region里面， 具体拷贝到哪里则取决于对象的年龄;
          如果达到一定的GC年龄，就会转移/提升到老年代中；否则就会转移到存活区。
          本次的存活区则会被加入到下一次年轻代GC/混合模式GC的CSet中。
        2、并发标记（Concurrent Marking）
            并发标记周期执行完毕之后，G1则会从纯年轻模式切换到混合模式。
        3、转移暂停: 混合模式（Evacuation Pause (mixed)）
            在执行混合模式的垃圾收集时，G1会选择一部分老年代region加入CSet，当然，每次的CSet都包括所有eden区和存活区。
            经过多次混合模式的垃圾收集之后，很多老年代region其实已经处理过了，然后G1又切换回纯年轻代模式，直到下一次的并发标记周期完成。
        ```
    - G1 GC回收效率很大程度上取决于分析GC日志信息，并调优回收参数。
    - G1 GC常用参数
        ```
        -XX:G1HeapRegionSize=n
        用来设置G1 region 的大小。 必须是2的幂（x次方)，允许的范围是 1MB 至 32MB。
        这个参数的默认值, 会根据堆内存的初始大小(-Xms)与最大值(-Xmx)动态调整，以便将堆内存切分为2048个左右的region。
        
        -XX:MaxGCPauseMillis=200
        预期G1每次执行GC操作的暂停时间，单位是毫秒，默认值是200毫秒，G1会尽量保证控制在这个范围内。老师通常设置为50毫秒。
        
        -XX:G1NewSizePercent=5
        设置年轻代的最小空间占比, 默认值为5，相当于最少有5%的堆内存会作为年轻代来使用。
        这个参数会覆盖 -XX:DefaultMinNewGenPercent。
        这是实验性质的参数，后续版本有可能会有变更。
        
        -XX:G1MaxNewSizePercent=60
        设置年轻代的最大空间占比。 默认值为60，相当于最多有60%的堆内存会作为年轻代来使用。
        此设置会覆盖 -XX:DefaultMaxNewGenPercent。
        这是实验性质的参数，后续版本有可能会有变更。
        
        -XX:ParallelGCThreads=n
        设置STW阶段的并行worker线程数。
        
        如果逻辑处理器小于等于8个，则默认 n 等于逻辑处理器的数量。
        如果逻辑处理器大于8个，则 n 默认约等于处理器数量的5/8 + 3。
        如果是高配置的 SPARC 系统，则默认 n 大约等于逻辑处理器数量的5/16。
        大多数情况下使用默认值即可。
        有一种情况除外，就是Docker容器中使用了低版本JDK，案例参考: JVM 问题排查分析下篇（案例实战）。
        -XX:ConcGCThreads=n
        设置并发标记的GC线程数。 默认值约等于 ParallelGCThreads 值的 1/4。
        
        -XX:InitiatingHeapOccupancyPercent=45
        设置标记周期的触发阈值, 即Java堆内存使用率的百分比。 默认的触发阈值是整个Java堆的45％。
        
        -XX:G1MixedGCLiveThresholdPercent=65
        执行混合模式GC时，根据老年代region的使用率，确定是否包含到回收集之中。 阈值默认为65％。
        此设置会覆盖 -XX:G1OldCSetRegionLiveThresholdPercent。
        这是实验性质的参数，后续版本有可能会有变更。
        
        -XX:G1HeapWastePercent=5
        设置可以容忍的堆内存浪费率百分比。
        如果可回收的堆内存占比小于这个阈值比例，则 HotSpot 不会启动混合模式GC。
        G1停止回收的最小内存大小，默认是堆大小的 5%。GC 会收集所有的 Region 中 的对象，但是如果下降到了 5%，就会停下来不再收集了。就是说，不必每次回收就把所有的垃圾都处理完，可以 遗留少量的下次处理，这样也降低了单次消耗的时间。
        
        -XX:G1MixedGCCountTarget=8
        在标记周期完成后，期望执行多少次混合模式的GC，直到存活数据的比例降到 G1MixedGCLiveThresholdPercent 之下。
        默认是执行8次混合模式的GC。 具体执行的次数一般都会小于这个值。
        
        -XX:G1OldCSetRegionThresholdPercent=10
        混合模式的GC中，每次处理的老年代 region 数量上限占比。 默认值为Java堆的10％。
        
        -XX:G1ReservePercent=10
        设置一定比例的保留空间, 让其保持空闲状态，降低to空间内存不足的风险。 默认值为 10％。
        虽然这是一个百分比，但实际会映射为具体的大小，所以当增加或减少百分比时，最好将Java堆的总大小也进行同样大小的调整。
        
        -XX:+GCTimeRatio
        这个参数就是计算花在Java应用线程上和花在 GC 线程上的时间比率，默认是9，跟新生代内存的分配比例一致。这个参数主要的目的是让用户可以控制花在应用上的时间，G1 的计算公式是 100/（1+GCTimeRatio）。这样如果参数设置为9，则最多10%的时间会花在GC工作上面。Parallel GC的默认值是99，表示1%的时间被用在GC上面，这是因为Parallel GC贯穿整个GC，而G1则根据Region来进行划分，不需要全局性扫描整个内存堆。
        
      -XX:+UseStringDeduplication
        手动开启Java String对象的去重工作，这个是JDK8u20版本之后新增的参数，主要用于相同String避免重复申请内存，节约Region的使用。
        
        ```
    - 当G1 GC触发了Full GC，G1会退化使用Serial收集器来完成垃圾的清理工作，它仅仅使用单线程来完成GC工作，GC暂停时间将达到秒级别的。
        ```
        1.并发模式失败
        G1启动标记周期，但在Mix GC之前，老年代就被填满，这时候G1会放弃标记周期。
        解决办法:增加堆大小，或者调整周期（例如增加线程数-XX:ConcGCThreads等）。
        2.晋升失败
        没有足够的内存供存活对象或晋升对象使用，由此触发了Full GC(to-space exhausted/to-space overflow）
        解决办法:
        1)增加–XX:G1ReservePercent选项的值（并相应增加总的堆大小）增加预留内存量。
        2)通过减少–XX:InitiatingHeapOccupancyPercent提前启动标记周期。
        3)也可以通过增加–XX:ConcGCThreads选项的值来增加并行标记线程的数目。
        3.巨型对象分配失败
        当巨型对象找不到合适的空间进行分配时，就会启动Full GC，来释放空间。
        解决办法:增加内存或者增大-XX:G1HeapRegionSize
      
        ```
## 第四课 NIO 模型与 Netty 入门
1. （**选做**）运行课上的例子，以及 Netty 的例子，分析相关现象。
    > sb -u http://localhost:8801 -n 600000 -c 8
    ```
       Requests: 600000
       RPS: 4255.4
       90th Percentile: 1ms
       95th Percentile: 2ms
       99th Percentile: 3ms
       Average: 1ms
       Min: 0ms
       Max: 109ms
    ```
   > sb -u http://localhost:8802 -n 600000 -c 8
   ```
    Requests: 600000
    RPS: 3829.3
    90th Percentile: 1ms
    95th Percentile: 1ms
    99th Percentile: 3ms
    Average: 0.7ms
    Min: 0ms
    Max: 59ms
   ```
   > sb -u http://localhost:8803 -n 600000 -c 8
   ```
       Requests: 600000
       RPS: 4636.3
       90th Percentile: 0ms
       95th Percentile: 1ms
       99th Percentile: 2ms
       Average: 0.1ms
       Min: 0ms
       Max: 106ms
   ```
   > sb -u http://localhost:8808/test -n 600000 -c 8
   ```
       Requests: 600000
       RPS: 7478.6
       90th Percentile: 0ms
       95th Percentile: 0ms
       99th Percentile: 1ms
       Average: 0ms
       Min: 0ms
       Max: 59ms
   ```
   - 第一个例子io单线程执行 RPS 4255
   - 第二个例子io多线程执行，RPS 2829，但执行io时都要临时创建新的线程，这个开销很大，所用时间较多，一次虽然多个io操作同时执行，但RPS却比第一个例子要低。
   - 第三个例子使用线程池中的线程多线程执行io，RPS 4636，复用已存在线程，省去了第二个例子中每次都要创建新线程的时间，因此RPS要比第一个例子高一些。
   - 第四个例子使用了netty框架处理io，RPS 7478，使用了异步非阻塞io模型，大幅提高了程序性能。
2. （**必做**）写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801 ，代码提交到 GitHub。
    > HttpClient.java
    ```
       package java0.nio01;
       
       import org.apache.commons.codec.binary.StringUtils;
       import org.apache.http.HttpEntity;
       import org.apache.http.annotation.Contract;
       import org.apache.http.client.methods.CloseableHttpResponse;
       import org.apache.http.client.methods.HttpGet;
       import org.apache.http.client.methods.HttpPost;
       import org.apache.http.client.methods.HttpRequestBase;
       import org.apache.http.impl.client.CloseableHttpClient;
       import org.apache.http.impl.client.HttpClientBuilder;
       
       import java.io.IOException;
       
       /**
        * @Description
        * @auther Movo
        * @create 2021/1/19 15:37
        */
       public class HttpClient {
       
           public static void main(String[] args) {
               HttpClient httpClient = new HttpClient();
               httpClient.httpRequest(HttpMethod.GET.getMethod(), "http://localhost:8801");
           }
       
           public void httpRequest(String httpMethod, String uri) {
               HttpRequestBase request = null;
               if(HttpMethod.GET.getMethod().equals(httpMethod)) {
                   request = new HttpGet(uri);
               } else if (HttpMethod.POST.getMethod().equals(httpMethod)) {
                   request = new HttpPost(uri);
               }
               if(request != null) {
                   CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                   CloseableHttpResponse response = null;
                   try {
                       response = httpClient.execute(request);
                       HttpEntity responseEntity = response.getEntity();
                       System.out.println("响应状态:" + response.getStatusLine());
                       if(responseEntity != null) {
                           System.out.println("响应内容长度:" + responseEntity.getContentLength());
                           System.out.println("响应内容:" + StringUtils.newStringUtf8(responseEntity.getContent().readAllBytes()));
                       }
                   } catch (IOException e) {
                       e.printStackTrace();
                   } finally {
                       if (httpClient != null) {
                           try {
                               httpClient.close();
                           } catch (IOException e) {
                               e.printStackTrace();
                               httpClient = null;
                           }
                       }
                       if (response != null) {
                           try {
                               response.close();
                           } catch (IOException e) {
                               e.printStackTrace();
                               response = null;
                           }
                       }
                   }
               }
           }
       }
       
       enum HttpMethod {
           GET("get"), POST("post");
           private final String method;
           HttpMethod(String method) {
               this.method = method;
           }
           public String getMethod() {
               return method;
           }
       }

    ```
    >> 运行结果
    ```
        响应状态:HTTP/1.1 200 OK
        响应内容长度:9
        响应内容:hello,nio
   
    ```
   
