package com.sse.service.concurrent;

import lombok.Data;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-23 11:19
 */

@Data
public class CalculateStartStatus {
    private long startTimestamp;
    private long startSequence;

    public CalculateStartStatus(long startTimestamp, long startSequence) {
        this.startTimestamp = startTimestamp;
        this.startSequence = startSequence;
    }
}
