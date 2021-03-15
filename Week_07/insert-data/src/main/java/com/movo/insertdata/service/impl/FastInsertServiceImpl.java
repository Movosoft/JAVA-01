package com.movo.insertdata.service.impl;

import com.movo.insertdata.dao.FastInsertDao;
import com.movo.insertdata.service.FastInsertService;
import com.movo.insertdata.utils.CommonUtils;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
@Service
public class FastInsertServiceImpl implements FastInsertService {

    private FastInsertDao fastInsertDao;

    public FastInsertServiceImpl(final FastInsertDao fastInsertDao) {
        this.fastInsertDao = fastInsertDao;
    }

    @Override
    public void insert(long num) {
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = CommonUtils.getDefaultExecutorService();
        // 插入前更换数据库引擎 InnoDB 换 MYISAM
        // 插入前去掉所有表中索引，省去建立索引的时间,因MYISAM会延迟更新索引，因此可忽略此操作
        // 按理说前两步操作会提升插入速度 但是事实上缺会使插入速度大大降低 对此不由产生疑问
        // set global sync_binlog=0;默认值为1
        // 建立任务 任务数为cores
        int taskNum = cores;
        // 根据taskNum 分组num
        if(num < taskNum) {
            taskNum = (int)num;
        }
        long groupNum = num / taskNum;
        long remainder = num % taskNum;
        Long opNum;
        Long startNum;
        for(int i = 1;i <= taskNum;i++) {
            startNum = (i - 1) * groupNum;
            if(i == taskNum) {
                opNum = groupNum + remainder;
            } else {
                opNum = groupNum;
            }
            InsertRunnable runnable = new InsertRunnable(fastInsertDao, startNum, opNum);
            executorService.execute(runnable);
        }
        executorService.shutdown();
    }
}

class InsertRunnable implements Runnable {

    private FastInsertDao fastInsertDao;
    private long startNum;
    private long opNum;


    public InsertRunnable(final FastInsertDao fastInsertDao, final long startNum, final long opNum) {
        this.fastInsertDao = fastInsertDao;
        this.startNum = startNum;
        this.opNum = opNum;
    }

    @Override
    public void run() {
        try {
            fastInsertDao.batchExecute(startNum, opNum);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            System.out.println(System.currentTimeMillis());
        }
    }
}
