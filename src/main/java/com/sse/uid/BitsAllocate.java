package com.sse.uid;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-20 20:35
 */

@Data
public class BitsAllocate {

    /**
     * Total 64 bits
     */
    public static final int TOTAL_BITS = 1 << 6;

    /**
     * Bits for [sign-> millisecond-> workId-> sequence]
     */
    private int signBits = 1;
    private int timestampBits;
    private int workerIdBits;
    private int sequenceBits;

    /**
     * Max value for workId & sequence
     */
    private long maxDeltaMilliSeconds;
    private long maxWorkerId;
    private long maxSequence;

    /**
     * Shift for timestamp & workerId
     */
    private int timestampShift;
    private int workerIdShift;

    /**
     * Constructor with timestampBits, workerIdBits, sequenceBits<br>
     * The highest bit used for sign, so <code>63</code> bits for timestampBits, workerIdBits, sequenceBits
     */
    public BitsAllocate(int timestampBits, int workerIdBits, int sequenceBits) {
        // make sure allocated 64 bits
        int allocateTotalBits = signBits + timestampBits + workerIdBits + sequenceBits;
        Assert.isTrue(allocateTotalBits == TOTAL_BITS, "allocate not enough 64 bits");

        // initialize bits
        this.timestampBits = timestampBits;
        this.workerIdBits = workerIdBits;
        this.sequenceBits = sequenceBits;

        // initialize max value
        this.maxDeltaMilliSeconds = ~(-1L << timestampBits);
        this.maxWorkerId = ~(-1L << workerIdBits);
        this.maxSequence = ~(-1L << sequenceBits);

        // initialize shift
        this.timestampShift = workerIdBits + sequenceBits;
        this.workerIdShift = sequenceBits;
    }

    /**
     * Allocate bits for UID according to delta millisecond & workerId & sequence<br>
     * <b>Note that: </b>The highest bit will always be 0 for sign
     *
     * @param deltaMilSec 毫秒
     * @param workerId
     * @param sequence
     * @return
     */
    public long generateUid(long deltaMilSec, long workerId, long sequence) {
        return (deltaMilSec << timestampShift) | (workerId << workerIdShift) | sequence;
    }
}
