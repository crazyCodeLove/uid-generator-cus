package com.sse.uid;

import com.sse.exception.RTExceptionBase;

import java.util.List;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-20 20:51
 */

public interface UidGenerator {

    /**
     * Get a unique ID
     *
     * @return Uid
     * @throws RTExceptionBase
     */
    long getUid() throws RTExceptionBase;

    /**
     * get batch Uids
     *
     * @param batchNumber
     * @return
     */
    List<Long> getUidBatch(int batchNumber) throws RTExceptionBase;

    /**
     * Parse the UID into elements which are used to generate the UID. <br>
     * Such as timestamp & workerId & sequence...
     *
     * @param uid
     * @return Parsed info
     */
    String parseUid(long uid);
}
