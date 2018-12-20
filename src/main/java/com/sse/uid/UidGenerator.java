package com.sse.uid;

import com.sse.exception.UidGenerateException;

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
     * @return UID
     * @throws UidGenerateException
     */
    long getUID() throws UidGenerateException;

    /**
     * get batch Uids
     * @param batchNumber
     * @return
     */
    long[] getUidBatch(int batchNumber) throws UidGenerateException;

    /**
     * Parse the UID into elements which are used to generate the UID. <br>
     * Such as timestamp & workerId & sequence...
     *
     * @param uid
     * @return Parsed info
     */
    String parseUID(long uid);
}
