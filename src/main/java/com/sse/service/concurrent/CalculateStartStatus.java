package com.sse.service.concurrent;

import lombok.Getter;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-23 11:19
 */

@Getter
public class CalculateStartStatus {
    private final long startTimestamp;
    private final long startSequence;

    public CalculateStartStatus(long startTimestamp, long startSequence) {
        this.startTimestamp = startTimestamp;
        this.startSequence = startSequence;
    }
}
