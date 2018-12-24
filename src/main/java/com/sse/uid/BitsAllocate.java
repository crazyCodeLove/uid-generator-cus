package com.sse.uid;

import lombok.Data;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

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
    private int workIdBits;
    private int sequenceBits;

    /**
     * Max value for workId & sequence
     */
    private long maxDeltaMilliSeconds;
    private long maxWorkId;
    private long maxSequence;

    /**
     * Shift for timestamp & workerId
     */
    private int timestampShift;
    private int workerIdShift;

    /**
     * Constructor with timestampBits, workIdBits, sequenceBits<br>
     * The highest bit used for sign, so <code>63</code> bits for timestampBits, workIdBits, sequenceBits
     */
    public BitsAllocate(int timestampBits, int workIdBits, int sequenceBits) {
        // make sure allocated 64 bits
        int allocateTotalBits = signBits + timestampBits + workIdBits + sequenceBits;
        Assert.isTrue(allocateTotalBits == TOTAL_BITS, "allocate not enough 64 bits");

        // initialize bits
        this.timestampBits = timestampBits;
        this.workIdBits = workIdBits;
        this.sequenceBits = sequenceBits;

        // initialize max value
        this.maxDeltaMilliSeconds = ~(-1L << timestampBits);
        this.maxWorkId = ~(-1L << workIdBits);
        this.maxSequence = ~(-1L << sequenceBits);

        // initialize shift
        this.timestampShift = workIdBits + sequenceBits;
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

    /**
     * get all uid at the deltaMilSec, and start sequence is startSequence
     * sequenceBits must not more than 31
     * @param deltaMilSec
     * @param workerId
     * @param startSequence
     * @return
     */
    public List<Long> generateAllUidAtOneTimestamp(long deltaMilSec, long workerId, long startSequence) {
        long startUid = (deltaMilSec << timestampShift) | (workerId << workerIdShift) | startSequence;
        List<Long> allUids = new ArrayList<>((int)(getMaxSequence() - startSequence)<<1);
        allUids.add(startUid);
        for (long i = startSequence; i < getMaxSequence(); i++) {
            allUids.add(startSequence + i);
        }
        return allUids;
    }
}
