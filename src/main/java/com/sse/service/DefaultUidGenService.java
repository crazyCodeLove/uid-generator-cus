package com.sse.service;

import com.sse.exception.UidGenerateException;
import com.sse.uid.UidGenerator;
import org.springframework.stereotype.Service;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-20 20:54
 */

@Service
public class DefaultUidGenService implements UidGenerator {


    @Override
    public long getUID() throws UidGenerateException {
        return 0;
    }

    @Override
    public long[] getUidBatch(int batchNumber) {
        if (batchNumber <= 0) {
            batchNumber = 1;
        }
        long[] result = new long[batchNumber];
        for (int i = 0; i < batchNumber; i++) {
            result[i] = getUID();
        }
        return result;
    }

    @Override
    public String parseUID(long uid) {
        return null;
    }
}
