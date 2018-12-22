package com.sse.service;

import com.sse.uid.UidGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pczhao
 * @email
 * @date 2018-12-21 8:37
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultUidGenServiceTest {

    @Autowired
    @Qualifier(value = "DefaultUidGenService")
    private UidGenerator uidGenService;

    private final int COUNT = 10000000; // 1000w 测试生成数量

    @Test
    public void getUidSerialTest() {
        long startTime = System.currentTimeMillis();
        HashSet<Long> uids = new HashSet<>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            generateUid(uids);
        }
        Assert.assertTrue(uids.size() == COUNT);
        System.out.println("last time(ms):" + (System.currentTimeMillis() - startTime));
    }

    @Test
    public void getUidParallelTest() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        int THREADS = Math.max(Runtime.getRuntime().availableProcessors() << 1, 1);
        System.out.println("threads:" + THREADS);
        AtomicInteger control = new AtomicInteger(-1);
        Set<Long> uidSet = new ConcurrentSkipListSet<>();
        // Initialize threads
        List<Thread> threadList = new ArrayList<>(THREADS);
        for (int i = 0; i < THREADS; i++) {
            Thread thread = new Thread(() -> workerRun(uidSet, control));
            thread.setName("UID-generator-" + i);

            threadList.add(thread);
            thread.start();
        }
        // Wait for worker done
        for (Thread thread : threadList) {
            thread.join();
        }
        Assert.assertEquals(COUNT, control.get());
        Assert.assertEquals(COUNT, uidSet.size());
        System.out.println("last time(ms):" + (System.currentTimeMillis() - startTime));
    }

    /**
     * Worker run
     */
    private void workerRun(Set<Long> uidSet, AtomicInteger control) {
        for (;;) {
            int myPosition = control.updateAndGet(old -> (old == COUNT ? COUNT : old + 1));
            if (myPosition == COUNT) {
                return;
            }
            generateUid(uidSet);
        }
    }

    /**
     * 获取一个 Uid
     * @param uids
     */
    private void generateUid(Set<Long> uids) {
        uids.add(uidGenService.getUid());
    }

}
