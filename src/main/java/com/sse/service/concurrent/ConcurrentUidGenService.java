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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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
    private ConcurrentHashMap<Long, AtomicReference<CalculateStartStatus>> timestampStatus = new ConcurrentHashMap<>();

    /**
     * 使用 getUidBatch 方法的线程数量统计
     */
    private volatile AtomicInteger calThreadCount = new AtomicInteger(0);

    @Override
    public long getUid() throws RTException {
        return 0;
    }

    @Override
    public List<Long> getUidBatch(int batchNumber) throws RTException {
        if (batchNumber <= 0) {
            batchNumber = 1;
        }
        calThreadCount.getAndIncrement();
        List<Long> result = new ArrayList<>(batchNumber * 2);
        List<UidBatchSequenceRange> needToMake = new ArrayList<>();
        do {
            long currentMilliSecond = uidGenBase.getCurrentMilliSecond();
            CalculateStartStatus calStartStatus;
            AtomicReference<CalculateStartStatus> calStartStatusRef = timestampStatus.get(currentMilliSecond);
            if (calStartStatusRef == null) {
                // 当前时间还没有被使用
                calStartStatus = new CalculateStartStatus(currentMilliSecond, 0L);
                calStartStatusRef = new AtomicReference<>(calStartStatus);
                AtomicReference<CalculateStartStatus> putResult = timestampStatus.putIfAbsent(currentMilliSecond, calStartStatusRef);
                if (putResult != null) {//没有添加成功
                    continue;
                }
            } else {
                calStartStatus = calStartStatusRef.get();
            }
            // 成功获取 calStartStatus
            long availableUidNumber = uidGenBase.getBitsAllocate().getMaxSequence() - calStartStatus.getStartSequence() + 1;
            if (availableUidNumber == 0) {
                // 当前时间的可用序列为0，需要等到下个时间序列
                uidGenBase.getNextMilliSecond(calStartStatus.getStartTimestamp());
                continue;
            }
            UidBatchSequenceRange uidBatchSequenceRange;
            if (batchNumber >= availableUidNumber) {
                // 需要的 >= 可提供的，序列号不够使用
                uidBatchSequenceRange = new UidBatchSequenceRange(
                        calStartStatus.getStartTimestamp(),
                        uidGenBase.getWorkNodeId(),
                        calStartStatus.getStartSequence(),
                        uidGenBase.getBitsAllocate().getMaxSequence());
                CalculateStartStatus calEndStatus = new CalculateStartStatus(
                        calStartStatus.getStartTimestamp(),
                        uidGenBase.getBitsAllocate().getMaxSequence() + 1);
                if (calStartStatusRef.compareAndSet(calStartStatus, calEndStatus)) {
                    // 替换成功
                    batchNumber -= availableUidNumber;
                    needToMake.add(uidBatchSequenceRange);
                }
            } else {
                // 需要的 < 可提供的，还有冗余
                uidBatchSequenceRange = new UidBatchSequenceRange(
                        calStartStatus.getStartTimestamp(),
                        uidGenBase.getWorkNodeId(),
                        calStartStatus.getStartSequence(),
                        calStartStatus.getStartSequence() + batchNumber - 1);
                CalculateStartStatus calEndStatus = new CalculateStartStatus(
                        calStartStatus.getStartTimestamp(),
                        calStartStatus.getStartSequence() + batchNumber);
                if (calStartStatusRef.compareAndSet(calStartStatus, calEndStatus)) {
                    // 替换成功
                    batchNumber = 0;
                    needToMake.add(uidBatchSequenceRange);
                }
            }
        } while (batchNumber > 0);
        for (UidBatchSequenceRange range :
                needToMake) {
            List<Long> uidBatch = uidGenBase.getUidBatch(range);
            result.addAll(uidBatch);
        }
        calThreadCount.getAndDecrement();
        return result;
    }

    @Override
    public String parseUid(long uid) {
        return null;
    }
}
