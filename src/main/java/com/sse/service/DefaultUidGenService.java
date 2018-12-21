package com.sse.service;

import com.sse.config.UidGeneratorConfig;
import com.sse.exception.RTException;
import com.sse.uid.BitsAllocate;
import com.sse.uid.UidGenerator;
import com.sse.uid.WorkNodeAssigner;
import com.sse.util.DateTimeUtil;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-20 20:54
 */

@Service
public class DefaultUidGenService implements UidGenerator {

    @Autowired
    private UidGeneratorConfig uidGeneratorConfig;

    private long epochMilliSeconds  = DateTimeUtil.parseByDayPattern(uidGeneratorConfig.getEpochStr()).getTime();

    @Autowired
    private BitsAllocate bitsAllocate;

    @Autowired
    private WorkNodeAssigner workNodeService;

    @Getter
    private int workNodeId = workNodeService.getWorkNodeId();

    /** Volatile fields caused by nextId() */
    protected long sequence = 0L;
    protected long lastMilliSeconds = -1L;


    @Override
    public long getUID() throws RTException {
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
