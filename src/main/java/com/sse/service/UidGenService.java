package com.sse.service;

import com.sse.exception.RTException;
import com.sse.service.concurrent.ConcurrentUidGenService;
import com.sse.uid.UidGenerator;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pczhao
 * @email
 * @date 2018-12-24 15:51
 */

@Service(value = "UidGenService")
public class UidGenService implements UidGenerator {
    private static final int START_MULTI_THREAD_BATCH_NUMBER_THREASHOLD = 10000;

    @Getter
    @Autowired
    private DefaultUidGenService defUidGenService;

    @Getter
    @Autowired
    private ConcurrentUidGenService conUidGenService;


    @Override
    public long getUid() throws RTException {
        return getUidBatch(1).get(0);
    }

    @Override
    public List<Long> getUidBatch(int batchNumber) throws RTException {
        if (batchNumber <= 0) {
            batchNumber = 1;
        }
        int processorsNum = Runtime.getRuntime().availableProcessors();

        if (batchNumber <= START_MULTI_THREAD_BATCH_NUMBER_THREASHOLD || processorsNum < 10) {
            return defUidGenService.getUidBatch(batchNumber);
        } else {
            int THREADS = Math.max(processorsNum << 1, 1);
            List<Long> result = new ArrayList<>(batchNumber << 1);
            int batchSize = 1000;
            int executeNum = batchNumber / batchSize;
            Set<Long> tuids;
            try {
                tuids = parallelGenUid(THREADS, batchSize, executeNum);
            } catch (InterruptedException e) {
                throw new RTException(e);
            }
            result.addAll(tuids);
            if (batchNumber > result.size()) {
                result.addAll(defUidGenService.getUidBatch(batchNumber - result.size()));
            }
            return result;
        }
    }

    @Override
    public String parseUid(long uid) {
        return defUidGenService.parseUid(uid);
    }

    /**
     * 例如想要生产 1000 个uid，每次生产 100 个，执行 10 次即可生成 1000 个uid。
     * 此时可以设置成 batchSize = 100，executeNum = 10
     *
     * @param threads    线程数
     * @param batchSize  执行一次生成的数量
     * @param executeNum 执行次数
     * @return
     * @throws InterruptedException
     */
    public Set<Long> parallelGenUid(final int threads, final int batchSize, final int executeNum) throws InterruptedException {
        AtomicInteger control = new AtomicInteger(-1);
        Set<Long> uidSet = new ConcurrentSkipListSet<>();
        // Initialize threads
        List<Thread> threadList = new ArrayList<>(threads);
        for (int i = 0; i < threads; i++) {
            Thread thread = new Thread(() -> workerRunBatch(uidSet, control, batchSize, executeNum));
            thread.setName("UID-generator-" + i);
            threadList.add(thread);
            thread.start();
        }
        // Wait for worker done
        for (Thread thread : threadList) {
            thread.join();
        }
        return uidSet;
    }

    /**
     * Worker run
     */
    private void workerRunBatch(Set<Long> uidSet, AtomicInteger control, int batch, final int executeNum) {
        for (; ; ) {
            int myPosition = control.updateAndGet(old -> (old == executeNum ? executeNum : old + 1));
            if (myPosition >= executeNum) {
                return;
            }
            generateUidBatch(uidSet, batch);
        }
    }

    /**
     * 获取一批 Uid
     *
     * @param uids
     * @param batch
     */
    private void generateUidBatch(Set<Long> uids, int batch) {
        uids.addAll(conUidGenService.getUidBatch(batch));
    }
}
