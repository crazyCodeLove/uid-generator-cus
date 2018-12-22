package com.sse.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

/**
 * @author pczhao
 * @email
 * @date 2018-12-21 8:37
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultUidGenServiceTest {

    @Autowired
    private DefaultUidGenService uidGenService;

    @Test
    public void getUidTest() {
        long startTime = System.currentTimeMillis();
        int count = 10000000; // 1000w 测试生成数量
        HashSet<Long> uids = new HashSet<>(count);
        for (int i = 0; i < count; i++) {
            uids.add(uidGenService.getUid());
        }
        Assert.assertTrue(uids.size() == count);
        System.out.println("last time(ms):" + (System.currentTimeMillis() - startTime));
    }

}
