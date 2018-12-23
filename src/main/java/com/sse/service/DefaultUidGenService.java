package com.sse.service;

import com.sse.exception.RTException;
import com.sse.exception.UidGenerateException;
import com.sse.model.UidBatchSequenceRange;
import com.sse.uid.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        if (currentMilliSecond < lastMilliSeconds) {
            throw new UidGenerateException(String.format("Clock moved backwards. Refusing for %d seconds", lastMilliSeconds - currentMilliSecond));
        }
        // At the same second, increase sequence
        if (currentMilliSecond > lastMilliSeconds) {
            // At the different milliSecond, sequence restart from zero
            sequence = 0L;
        }
        result = uidGenBase.getBitsAllocate().generateUid(
                currentMilliSecond - uidGenBase.getEpochMilliSeconds(),
                uidGenBase.getWorkNodeId(),
                sequence);

        sequence = (sequence + 1) & uidGenBase.getBitsAllocate().getMaxSequence();
        // Exceed the max sequence, we wait the next milliSecond to generate uid
        if (sequence == 0) {
            currentMilliSecond = uidGenBase.getNextMilliSecond(lastMilliSeconds);
        }
        lastMilliSeconds = currentMilliSecond;
        // Allocate bits for UID
        return result;
    }

    /**
     * 谁先抢到锁，先分配。
     *
     * @param batchNumber
     * @return
     * @throws RTException
     */
    @Override
    public List<Long> getUidBatch(int batchNumber) throws RTException {
        if (batchNumber <= 0) {
            batchNumber = 1;
        }
        List<Long> result = new ArrayList<>(batchNumber * 2);
        List<UidBatchSequenceRange> needToMake = new ArrayList<>();
        synchronized (this) {
            while (batchNumber > 0) {
                long currentMilliSecond = uidGenBase.getCurrentMilliSecond();
                // Clock moved backwards exception
                if (currentMilliSecond < lastMilliSeconds) {
                    throw new UidGenerateException(String.format("Clock moved backwards. Refusing for %d seconds", lastMilliSeconds - currentMilliSecond));
                }
                if (currentMilliSecond > lastMilliSeconds) {
                    // At the different milliSecond, sequence restart from zero
                    sequence = 0L;
                }
                // at current time available sequence number
                long availableUidNumber = uidGenBase.getBitsAllocate().getMaxSequence() - sequence + 1;
                UidBatchSequenceRange uidBatchSequenceRange;
                if (batchNumber >= availableUidNumber) {
                    // 需要的 >= 可提供的
                    uidBatchSequenceRange = new UidBatchSequenceRange(currentMilliSecond, uidGenBase.getWorkNodeId(), sequence, uidGenBase.getBitsAllocate().getMaxSequence());
                    sequence = 0L;
                    batchNumber -= availableUidNumber;
                    uidGenBase.getNextMilliSecond(currentMilliSecond);
                } else {
                    // 需要的 < 可提供的，还有冗余
                    uidBatchSequenceRange = new UidBatchSequenceRange(currentMilliSecond, uidGenBase.getWorkNodeId(), sequence, sequence + batchNumber - 1);
                    sequence += batchNumber;
                    batchNumber = 0;
                }
                needToMake.add(uidBatchSequenceRange);
                lastMilliSeconds = currentMilliSecond;
            }
        }
        for (UidBatchSequenceRange range :
                needToMake) {
            List<Long> uidBatch = uidGenBase.getUidBatch(range);
            result.addAll(uidBatch);
        }
        return result;
    }

    @Override
    public String parseUid(long uid) {
        return uidGenBase.parseUid(uid);
    }
}
