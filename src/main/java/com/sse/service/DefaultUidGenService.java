package com.sse.service;

import com.sse.exception.RTException;
import com.sse.uid.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-20 20:54
 */

@Service(value = "DefaultUidGenService")
public class DefaultUidGenService implements UidGenerator {

    @Autowired
    private UidGenServiceBase uidGenBase;

    /**
     * sequence number
     */
    private volatile long sequence = 0L;

    /**
     * last get uid timestamp
     */
    private volatile long lastMilliSeconds = -1L;


    @Override
    public synchronized long getUid() throws RTException {
        long currentMilliSecond = uidGenBase.getCurrentMilliSecond();
        // Clock moved backwards, refuse to generate uid
        long result = 0;
        long sequenceNew = 0;
        while (currentMilliSecond < lastMilliSeconds) {
            currentMilliSecond = uidGenBase.getCurrentMilliSecond();
        }
        // At the same second, increase sequence
        if (currentMilliSecond == lastMilliSeconds) {
            sequence = (sequence + 1) & uidGenBase.getBitsAllocate().getMaxSequence();
            // Exceed the max sequence, we wait the next milliSecond to generate uid
            if (sequence == 0) {
                currentMilliSecond = uidGenBase.getNextMilliSecond(lastMilliSeconds);
            }
            // At the different milliSecond, sequence restart from zero
        } else {
            sequence = 0L;
        }
        sequenceNew = sequence;
        result = uidGenBase.getBitsAllocate().generateUid(
                currentMilliSecond - uidGenBase.getEpochMilliSeconds(),
                uidGenBase.getWorkNodeId(),
                sequenceNew);
        lastMilliSeconds = currentMilliSecond;
        // Allocate bits for UID
        return result;
    }

    @Override
    public long[] getUidBatch(int batchNumber) throws RTException {
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
        return uidGenBase.parseUid(uid);
    }
}
