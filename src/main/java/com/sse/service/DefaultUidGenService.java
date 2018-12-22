package com.sse.service;

import com.sse.config.UidGeneratorConfig;
import com.sse.exception.RTException;
import com.sse.exception.UidGenerateException;
import com.sse.uid.BitsAllocate;
import com.sse.uid.UidGenerator;
import com.sse.uid.WorkNodeAssigner;
import com.sse.util.DateTimeUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-20 20:54
 */

@Service
public class DefaultUidGenService implements UidGenerator, InitializingBean {
    @Autowired
    private UidGeneratorConfig uidGeneratorConfig;

    @Autowired
    private BitsAllocate bitsAllocate;

    @Autowired
    private WorkNodeAssigner workNodeService;

    /**
     * 起始日期对应的毫秒
     */
    private long epochMilliSeconds;

    /**
     * server work node id
     */
    private int workNodeId;

    private volatile long sequence = 0L;
    private volatile long lastMilliSeconds = -1L;


    @Override
    public long getUid() throws RTException {
        long currentMilliSecond = getCurrentMilliSecond();
        // Clock moved backwards, refuse to generate uid
        if (currentMilliSecond < lastMilliSeconds) {
            long refusedSeconds = lastMilliSeconds - currentMilliSecond;
            throw new UidGenerateException(String.format("Clock moved backwards. Refusing for %d seconds", refusedSeconds));
        }
        long result = 0;
        long sequenceNew = 0;
        synchronized (this) {
            // At the same second, increase sequence
            if (currentMilliSecond == lastMilliSeconds) {
                sequence = (sequence + 1) & bitsAllocate.getMaxSequence();
                // Exceed the max sequence, we wait the next milliSecond to generate uid
                if (sequence == 0) {
                    currentMilliSecond = getNextMilliSecond(lastMilliSeconds);
                }
                // At the different milliSecond, sequence restart from zero
            } else {
                sequence = 0L;
            }
            sequenceNew = sequence;
        }
        result = bitsAllocate.generateUid(currentMilliSecond - epochMilliSeconds, workNodeId, sequenceNew);
        lastMilliSeconds = currentMilliSecond;
        // Allocate bits for UID
        return result;
    }

    @Override
    public long[] getUidBatch(int batchNumber) {
        if (batchNumber <= 0) {
            batchNumber = 1;
        }
        long[] result = new long[batchNumber];
        for (int i = 0; i < batchNumber; i++) {
            result[i] = getUid();
        }
        return result;
    }

    @Override
    public String parseUid(long uid) {
        return null;
    }

    /**
     * Get current milliSecond
     */
    private long getCurrentMilliSecond() {
        long currentMilliSecond = System.currentTimeMillis();
        if (currentMilliSecond - epochMilliSeconds > bitsAllocate.getMaxDeltaMilliSeconds()) {
            throw new UidGenerateException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentMilliSecond);
        }
        return currentMilliSecond;
    }

    /**
     * Get next millisecond
     */
    private long getNextMilliSecond(long lastMilliSeconds) {
        long timestamp = getCurrentMilliSecond();
        while (timestamp <= lastMilliSeconds) {
            timestamp = getCurrentMilliSecond();
        }
        return timestamp;
    }

    /**
     * 在属性装配完毕后做的初始化工作。依赖于自动装配对象的方法。
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        workNodeId = workNodeService.getWorkNodeId();
        epochMilliSeconds = DateTimeUtil.parseByDayPattern(uidGeneratorConfig.getEpochStr()).getTime();
    }
}
