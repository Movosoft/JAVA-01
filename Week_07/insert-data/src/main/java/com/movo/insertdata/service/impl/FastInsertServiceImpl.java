package com.movo.insertdata.service.impl;

import com.movo.insertdata.service.FastInsertService;
import com.movo.insertdata.utils.CommonUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
@Service
public class FastInsertServiceImpl implements FastInsertService {
    private DataSource dataSource;

    public FastInsertServiceImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
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
        Long opNum = null;
        Long startNum = null;
        for(int i = 1;i <= taskNum;i++) {
            startNum = (i - 1) * groupNum;
            if(i == taskNum) {
                opNum = groupNum + remainder;
            } else {
                opNum = groupNum;
            }
            Connection conn = null;
            try {
                conn = dataSource.getConnection();
                InsertRunnable runnable = new InsertRunnable(startNum, opNum, conn);
                executorService.execute(runnable);
            } catch (SQLException e) {
                e.printStackTrace();
                if(conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException et) {
                        et.printStackTrace();
                        conn = null;
                    }
                }
            }
        }
        executorService.shutdown();
    }
}

class InsertRunnable implements Runnable {

    private long startNum;
    private long opNum;
    private Connection conn;

    public InsertRunnable(final long startNum, final long opNum, final Connection conn) {
        this.startNum = startNum;
        this.opNum = opNum;
        this.conn = conn;
    }

    @Override
    public void run() {
        // 设置一条insert插入的行数
        int rowsForOneInsert = 30;
        String sql = null;
        Random random = new Random();
        PreparedStatement pst = null;
        try {
            conn.setAutoCommit(false);
            for(int i = 1;i <= opNum;) {
                sql = "insert into dt_order (order_code,buyer_id,order_status,order_price_i,order_price_d,pay_price_i,pay_price_d,pay_complete_time,order_complete_time,create_time,last_update_time,delete_tag) values ";
                for(int k = 1;k <= rowsForOneInsert;k++) {
                    sql += "('" + String.format("%020d", startNum + i) + "'," + (random.nextInt(100) + 1) + "," + random.nextInt(2) + ",";
                    sql += random.nextInt(10000) + "," + random.nextInt(100) + "," + random.nextInt(10000) + "," + random.nextInt(100) + ",sysdate(),sysdate(),sysdate(),sysdate(),";
                    sql += "0)";
                    i++;
                    if(k == rowsForOneInsert || (i - 1) == opNum) {
                        sql += ";";
                        break;
                    } else {
                        sql += ",";
                    }
                }

                if(pst == null) {
                    pst = conn.prepareStatement("select 1;");
                }
                pst.addBatch(sql);
            }
            pst.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    pst = null;
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    conn = null;
                }
            }
            System.out.println(System.currentTimeMillis());
        }
    }
}
