package com.movo.shardingsphere.subdt.keygenerator;

import com.google.common.base.Preconditions;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

import lombok.Generated;
import lombok.SneakyThrows;
import org.apache.shardingsphere.sharding.algorithm.keygen.TimeService;
import org.apache.shardingsphere.sharding.spi.KeyGenerateAlgorithm;

public final class MySnowflakeKeyGenerateAlgorithm implements KeyGenerateAlgorithm {
    public static final long EPOCH;
    private static final String WORKER_ID_KEY = "worker-id";
    private static final String MAX_VIBRATION_OFFSET_KEY = "max-vibration-offset";
    private static final String MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS_KEY = "max-tolerate-time-difference-milliseconds";
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_BITS = 10L;
    private static final long SEQUENCE_MASK = 4095L;
    private static final long WORKER_ID_LEFT_SHIFT_BITS = 12L;
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = 22L;
    private static final long WORKER_ID_MAX_VALUE = 1024L;
    private static final long WORKER_ID = 0L;
    private static final int DEFAULT_VIBRATION_VALUE = 1;
    private static final int MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS = 10;
    private static TimeService timeService = new TimeService();
    private Properties props = new Properties();
    private long workerId;
    private int maxVibrationOffset;
    private int maxTolerateTimeDifferenceMilliseconds;
    private int sequenceOffset = -1;
    private long sequence;
    private long lastMilliseconds;

    public MySnowflakeKeyGenerateAlgorithm() {
    }

    public void init() {
        this.workerId = this.getWorkerId();
        this.maxVibrationOffset = this.getMaxVibrationOffset();
        this.maxTolerateTimeDifferenceMilliseconds = this.getMaxTolerateTimeDifferenceMilliseconds();
    }

    private long getWorkerId() {
        long result = Long.parseLong(this.props.getOrDefault("worker-id", 0L).toString());
        Preconditions.checkArgument(result >= 0L && result < 1024L, "Illegal worker id.");
        return result;
    }

    private int getMaxVibrationOffset() {
        int result = Integer.parseInt(this.props.getOrDefault("max-vibration-offset", 1).toString());
        Preconditions.checkArgument(result >= 0 && (long)result <= 4095L, "Illegal max vibration offset.");
        return result;
    }

    private int getMaxTolerateTimeDifferenceMilliseconds() {
        return Integer.parseInt(this.props.getOrDefault("max-tolerate-time-difference-milliseconds", 10).toString());
    }

    public synchronized Comparable<?> generateKey() {
        long currentMilliseconds = timeService.getCurrentMillis();
        if (this.waitTolerateTimeDifferenceIfNeed(currentMilliseconds)) {
            currentMilliseconds = timeService.getCurrentMillis();
        }

        if (this.lastMilliseconds == currentMilliseconds) {
            if (0L == (this.sequence = this.sequence + 1L & 4095L)) {
                currentMilliseconds = this.waitUntilNextTime(currentMilliseconds);
            }
        } else {
            this.vibrateSequenceOffset();
            Random random = new Random();
            this.sequence = random.nextInt(16);
        }

        this.lastMilliseconds = currentMilliseconds;
        return currentMilliseconds - EPOCH << 22 | this.workerId << 12 | this.sequence;
    }

    private boolean waitTolerateTimeDifferenceIfNeed(long currentMilliseconds) {
        try {
            if (this.lastMilliseconds <= currentMilliseconds) {
                return false;
            } else {
                long timeDifferenceMilliseconds = this.lastMilliseconds - currentMilliseconds;
                Preconditions.checkState(timeDifferenceMilliseconds < (long)this.maxTolerateTimeDifferenceMilliseconds, "Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds", this.lastMilliseconds, currentMilliseconds);
                Thread.sleep(timeDifferenceMilliseconds);
                return true;
            }
        } catch (InterruptedException var5) {
//            throw var5;
            var5.printStackTrace();
            return false;
        }
    }

    private long waitUntilNextTime(long lastTime) {
        long result;
        for(result = timeService.getCurrentMillis(); result <= lastTime; result = timeService.getCurrentMillis()) {
        }

        return result;
    }

    private void vibrateSequenceOffset() {
        this.sequenceOffset = this.sequenceOffset >= this.maxVibrationOffset ? 0 : this.sequenceOffset + 1;
    }

    public String getType() {
        return "MYSNOWFLAKE";
    }

    @Generated
    public static void setTimeService(TimeService timeService) {
        MySnowflakeKeyGenerateAlgorithm.timeService = timeService;
    }

    @Generated
    public Properties getProps() {
        return this.props;
    }

    @Generated
    public void setProps(Properties props) {
        this.props = props;
    }

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 10, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        EPOCH = calendar.getTimeInMillis();
    }
}
