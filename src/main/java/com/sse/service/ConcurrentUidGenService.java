package com.sse.service;

import com.sse.config.UidGeneratorConfig;
import com.sse.exception.RTException;
import com.sse.uid.BitsAllocate;
import com.sse.uid.UidGenerator;
import com.sse.uid.WorkNodeAssigner;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-22 13:00
 */

@Service(value = "ConcurrentUidGenService")
public class ConcurrentUidGenService implements UidGenerator {

    @Override
    public long getUid() throws RTException {
        return 0;
    }

    @Override
    public List<Long> getUidBatch(int batchNumber) throws RTException {
        return null;
    }

    @Override
    public String parseUid(long uid) {
        return null;
    }
}
