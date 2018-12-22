package com.sse.model;

import lombok.Getter;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-22 14:24
 */

@Getter
public class UidBatchSequenceRange {
    private final long currentMilliSecond;
    private final long workNodeId;
    /**
     * 可用的 sequence 起始序号
     */
    private final long sequenceStart;
    /**
     * 可用的 sequence 末尾序号
     */
    private final long sequenceEnd;

    public UidBatchSequenceRange(long currentMilliSecond, long workNodeId, long sequenceStart, long sequenceEnd) {
        this.currentMilliSecond = currentMilliSecond;
        this.workNodeId = workNodeId;
        this.sequenceStart = sequenceStart;
        this.sequenceEnd = sequenceEnd;
    }

    @Override
    public String toString() {
        return "UidBatchSequenceRange{" +
                "currentMilliSecond=" + currentMilliSecond +
                ", workNodeId=" + workNodeId +
                ", sequenceStart=" + sequenceStart +
                ", sequenceEnd=" + sequenceEnd +
                '}';
    }
}
