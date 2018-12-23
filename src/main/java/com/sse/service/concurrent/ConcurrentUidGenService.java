package com.sse.service.concurrent;

import com.sse.exception.RTException;
import com.sse.model.UidBatchSequenceRange;
import com.sse.service.UidGenServiceBase;
import com.sse.uid.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-22 13:00
 */

@Service(value = "ConcurrentUidGenService")
public class ConcurrentUidGenService implements UidGenerator {

    @Autowired
    private UidGenServiceBase uidGenBase;

    /**
     * 根据当前时间和序列做分段锁;
     */
    private volatile ConcurrentHashMap<Long, CalculateStartStatus> timestampStatus = new ConcurrentHashMap<>();

    @Override
    public long getUid() throws RTException {
        return 0;
    }

    @Override
    public List<Long> getUidBatch(int batchNumber) throws RTException {
        if (batchNumber <= 0) {
            batchNumber = 1;
        }
        List<Long> result = new ArrayList<>(batchNumber * 2);
        List<UidBatchSequenceRange> needToMake = new ArrayList<>();
        do {
            long currentMilliSecond = uidGenBase.getCurrentMilliSecond();
            if (timestampStatus.contains(currentMilliSecond)) {
                // 已经有线程使用当前时间进行计算了

                synchronized (timestampStatus.get(currentMilliSecond)) {
                    long recoveMilliSecond = uidGenBase.getCurrentMilliSecond();
                    if (recoveMilliSecond == currentMilliSecond) {
                        // 恢复时间还是挂起的时间
                        CalculateStartStatus calculateStatus = timestampStatus.get(currentMilliSecond);
                        long availableUidNumber = uidGenBase.getBitsAllocate().getMaxSequence() - calculateStatus.getStartSequence() + 1;
                        UidBatchSequenceRange uidBatchSequenceRange;
                        if (batchNumber >= availableUidNumber) {
                            // 需要的 >= 可提供的，序列号不够使用
                            uidBatchSequenceRange = new UidBatchSequenceRange(
                                    calculateStatus.getStartTimestamp(),
                                    uidGenBase.getWorkNodeId(),
                                    calculateStatus.getStartSequence(),
                                    uidGenBase.getBitsAllocate().getMaxSequence());
                            calculateStatus.setStartSequence(availableUidNumber);
                            batchNumber -= availableUidNumber;
                            uidGenBase.getNextMilliSecond(currentMilliSecond);
                        } else {
                            // 需要的 < 可提供的，还有冗余
                            uidBatchSequenceRange = new UidBatchSequenceRange(
                                    calculateStatus.getStartTimestamp(),
                                    uidGenBase.getWorkNodeId(),
                                    calculateStatus.getStartSequence(),
                                    calculateStatus.getStartSequence() + batchNumber - 1);
                            calculateStatus.setStartSequence(calculateStatus.getStartSequence() + batchNumber);
                            batchNumber = 0;
                        }
                        needToMake.add(uidBatchSequenceRange);
                        // 处理完后，当前时间节点已经超过了计算的时间节点。将计算的时间节点移除
                        if (uidGenBase.getCurrentMilliSecond() > calculateStatus.getStartTimestamp()) {
                            timestampStatus.remove(calculateStatus.getStartTimestamp());
                        }
                    } else {
                        timestampStatus.remove(currentMilliSecond);
                    }
                }
            } else {
                CalculateStartStatus calculateStatus = new CalculateStartStatus(currentMilliSecond, 0L);
                CalculateStartStatus putStatus = timestampStatus.putIfAbsent(currentMilliSecond, calculateStatus);
                if (putStatus != null) {
                    // 插入成功
                    // at current time available sequence number
                    long availableUidNumber = uidGenBase.getBitsAllocate().getMaxSequence() - calculateStatus.getStartSequence() + 1;
                    UidBatchSequenceRange uidBatchSequenceRange;
                    if (batchNumber >= availableUidNumber) {
                        // 需要的 >= 可提供的，序列号不够使用
                        uidBatchSequenceRange = new UidBatchSequenceRange(
                                calculateStatus.getStartTimestamp(),
                                uidGenBase.getWorkNodeId(),
                                calculateStatus.getStartSequence(),
                                uidGenBase.getBitsAllocate().getMaxSequence());
                        calculateStatus.setStartSequence(availableUidNumber);
                        batchNumber -= availableUidNumber;
                        uidGenBase.getNextMilliSecond(currentMilliSecond);
                    } else {
                        // 需要的 < 可提供的，还有冗余
                        uidBatchSequenceRange = new UidBatchSequenceRange(
                                calculateStatus.getStartTimestamp(),
                                uidGenBase.getWorkNodeId(),
                                calculateStatus.getStartSequence(),
                                calculateStatus.getStartSequence() + batchNumber - 1);
                        calculateStatus.setStartSequence(calculateStatus.getStartSequence() + batchNumber);
                        batchNumber = 0;
                    }
                    needToMake.add(uidBatchSequenceRange);
                    // 处理完后，当前时间节点已经超过了计算的时间节点。将计算的时间节点移除
                    if (uidGenBase.getCurrentMilliSecond() > calculateStatus.getStartTimestamp()) {
                        timestampStatus.remove(calculateStatus.getStartTimestamp());
                    }
                }
            }
        } while (batchNumber > 0);
        for (UidBatchSequenceRange range :
                needToMake) {
            List<Long> uidBatch = uidGenBase.getUidBatch(range);
            result.addAll(uidBatch);
        }
        return result;
    }

    public int fillUidBatchSequenceRange(int batchNumber, CalculateStartStatus calculateStatus, long currentMilliSecond, List<UidBatchSequenceRange> needToMake) {
        long availableUidNumber = uidGenBase.getBitsAllocate().getMaxSequence() - calculateStatus.getStartSequence() + 1;
        UidBatchSequenceRange uidBatchSequenceRange;
        if (batchNumber >= availableUidNumber) {
            // 需要的 >= 可提供的，序列号不够使用
            uidBatchSequenceRange = new UidBatchSequenceRange(
                    calculateStatus.getStartTimestamp(),
                    uidGenBase.getWorkNodeId(),
                    calculateStatus.getStartSequence(),
                    uidGenBase.getBitsAllocate().getMaxSequence());
            calculateStatus.setStartSequence(availableUidNumber);
            batchNumber -= availableUidNumber;
            uidGenBase.getNextMilliSecond(currentMilliSecond);
        } else {
            // 需要的 < 可提供的，还有冗余
            uidBatchSequenceRange = new UidBatchSequenceRange(
                    calculateStatus.getStartTimestamp(),
                    uidGenBase.getWorkNodeId(),
                    calculateStatus.getStartSequence(),
                    calculateStatus.getStartSequence() + batchNumber - 1);
            calculateStatus.setStartSequence(calculateStatus.getStartSequence() + batchNumber);
            batchNumber = 0;
        }
        needToMake.add(uidBatchSequenceRange);
        // 处理完后，当前时间节点已经超过了计算的时间节点。将计算的时间节点移除
        if (uidGenBase.getCurrentMilliSecond() > calculateStatus.getStartTimestamp()) {
            timestampStatus.remove(calculateStatus.getStartTimestamp());
        }
        return batchNumber;
    }


    @Override
    public String parseUid(long uid) {
        return null;
    }
}
