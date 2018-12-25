package com.sse.service;

import com.sse.uid.UidGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pczhao
 * @email
 * @date 2018-12-25 15:42
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CachedUidGenServiceTest {

    @Autowired
    @Qualifier(value = "CachedUidGenService")
    private UidGenerator uidGenService;

    private final int COUNT = 10000000; // 1000w 测试生成数量

    // 可用线程数
    private final int THREADS = Math.max(Runtime.getRuntime().availableProcessors() << 1, 1);


    @Test
    public void getUidBatchParallelTest() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        int batchSize = 100;
        System.out.println("threads:" + THREADS);
        AtomicInteger control = new AtomicInteger(-1);
        Set<Long> uidSet = new ConcurrentSkipListSet<>();
        // Initialize threads
        List<Thread> threadList = new ArrayList<>(THREADS);
        for (int i = 0; i < THREADS; i++) {
            Thread thread = new Thread(() -> workerRunBatch(uidSet, control, batchSize));
            thread.setName("UID-generator-" + i);
            threadList.add(thread);
            thread.start();
        }
        // Wait for worker done
        for (Thread thread : threadList) {
            thread.join();
        }
        System.out.println(uidSet.size());
        Assert.assertEquals(COUNT, uidSet.size());
        System.out.println("last time(ms):" + (System.currentTimeMillis() - startTime));
    }

    /**
     * Worker run
     */
    private void workerRunBatch(Set<Long> uidSet, AtomicInteger control, int batch) {
        for (; ; ) {
            int myPosition = control.updateAndGet(old -> (old == COUNT ? COUNT : old + 1));
            if (myPosition >= COUNT / batch) {
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
        uids.addAll(uidGenService.getUidBatch(batch));
    }

}
