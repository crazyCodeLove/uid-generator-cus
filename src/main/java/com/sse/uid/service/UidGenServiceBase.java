package com.sse.uid.service;

import com.sse.config.UidGeneratorConfig;
import com.sse.exception.UidGenerateException;
import com.sse.model.UidBatchSequenceRange;
import com.sse.model.WorkNodeEntity;
import com.sse.uid.BitsAllocate;
import com.sse.uid.WorkNodeAssigner;
import com.sse.util.DateTimeUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-22 13:33
 */

@EnableScheduling
@Service
@Data
public class UidGenServiceBase implements InitializingBean {
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

    /**
     * 每隔 40分钟 更新一下当前 workNodeId 的访问时间
     */
    @Scheduled(cron = "0 0/40 * * * ?")
    public void updateWorkNodeIdAccessTime() {
        workNodeService.updateWorkNodeAccessTime(workNodeId);
    }

    /**
     * 每隔一小时清理一次无效的 workNodeId。上次访问时间超过了默认的 INVALID_WORK_NODE_MAX_LAST_TIME_MINUTE = 60 分钟
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void removeInvalidateWorkNode() {
        List<WorkNodeEntity> allWorkNode = workNodeService.getAllWorkNodeLastUpdateTime();
        for (WorkNodeEntity entity : allWorkNode) {
            Date that = DateTimeUtil.addMinutes(entity.getLastUpdateTime(), uidGeneratorConfig.getINVALID_WORK_NODE_MAX_LAST_TIME_MINUTE());
            if (TimeUnit.MILLISECONDS.toMinutes(that.getTime()) < TimeUnit.MILLISECONDS.toMinutes(new Date().getTime())) {
                workNodeService.deleteWorkNode(entity.getId());
            }
        }
    }

    /**
     * @param range
     * @return
     */
    public List<Long> getUidBatch(UidBatchSequenceRange range) {
        long size = range.getSequenceEnd() - range.getSequenceStart() + 1;
        int acc_size = (int) size;
        List<Long> uids = new ArrayList<>(acc_size * 2);
        for (int i = 0; i < acc_size; i++) {
            uids.add(bitsAllocate.generateUid(range.getCurrentMilliSecond() - epochMilliSeconds,
                    range.getWorkNodeId(),
                    range.getSequenceStart() + i));
        }
        return uids;
    }

    /**
     * Get current milliSecond
     */
    public long getCurrentMilliSecond() {
        long currentMilliSecond = System.currentTimeMillis();
        if (currentMilliSecond - epochMilliSeconds > bitsAllocate.getMaxDeltaMilliSeconds()) {
            throw new UidGenerateException("Timestamp bits is exhausted. Refusing UID generate. Now: " + currentMilliSecond);
        }
        return currentMilliSecond;
    }

    /**
     * Get next millisecond
     */
    public long getNextMilliSecond(long lastMilliSeconds) {
        long timestamp = getCurrentMilliSecond();
        while (timestamp <= lastMilliSeconds) {
            timestamp = getCurrentMilliSecond();
        }
        return timestamp;
    }

    public String parseUid(long uid) {
        long totalBits = bitsAllocate.TOTAL_BITS;
        long signBits = bitsAllocate.getSignBits();
        long timestampBits = bitsAllocate.getTimestampBits();
        long workIdBits = bitsAllocate.getWorkIdBits();
        long sequenceBits = bitsAllocate.getSequenceBits();

        // parse UID
        long sequence = (uid << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
        long workerId = (uid << (timestampBits + signBits)) >>> (totalBits - workIdBits);
        long deltaSeconds = uid >>> (workerId + sequenceBits);

        Date thatTime = new Date(TimeUnit.MILLISECONDS.toMillis(epochMilliSeconds + deltaSeconds));
        String thatTimeStr = DateTimeUtil.formatByDateTimeMsPattern(thatTime);

        // format as string
        return String.format("{\"Uid\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"sequence\":\"%d\"}",
                uid, thatTimeStr, workerId, sequence);
    }

    /**
     * 在属性装配完毕后做的初始化工作。依赖于自动装配对象的方法。
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        workNodeId = workNodeService.getWorkNodeId();
        epochMilliSeconds = DateTimeUtil.parseByDayPattern(uidGeneratorConfig.getEpochStr()).getTime();
    }
}
