package com.sse.uid.service;

import com.sse.exception.RTExceptionBase;
import com.sse.uid.service.cache.RingBuffer;
import com.sse.uid.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pczhao
 * @email
 * @date 2018-12-24 20:18
 */

@Service(value = "CachedUidGenService")
public class CachedUidGenService implements UidGenerator {

    @Autowired
    private RingBuffer ringBuffer;

    @Override
    public long getUid() throws RTExceptionBase {
        return getUidBatch(1).get(0);
    }

    @Override
    public List<Long> getUidBatch(int batchNumber) throws RTExceptionBase {
        return ringBuffer.take(batchNumber);
    }

    @Override
    public String parseUid(long uid) {
        return ringBuffer.getUidGenService().parseUid(uid);
    }
}
