package com.sse.service;

import com.sse.exception.RTException;
import com.sse.service.catche.RingBuffer;
import com.sse.uid.UidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pczhao
 * @email
 * @date 2018-12-24 20:18
 */

@Service(value = "CatchedUidGenService")
public class CachedUidGenService implements UidGenerator {

    @Autowired
    private RingBuffer ringBuffer;

    @Override
    public long getUid() throws RTException {
        return getUidBatch(1).get(0);
    }

    @Override
    public List<Long> getUidBatch(int batchNumber) throws RTException {
        return ringBuffer.take(batchNumber);
    }

    @Override
    public String parseUid(long uid) {
        return ringBuffer.getUidGenService().parseUid(uid);
    }
}
