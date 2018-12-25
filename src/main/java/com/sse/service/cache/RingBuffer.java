package com.sse.service.cache;

import com.sse.service.UidGenService;
import com.sse.service.UidGenServiceBase;
import lombok.Getter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author pczhao
 * @email
 * @date 2018-12-25 9:06
 */

@Component("RingBuffer")
public class RingBuffer implements InitializingBean, DisposableBean {
    public static final int DEFAULT_BOOST_POWER = 3;
    public static final int DEFAULT_PADDING_PERCENT = 70; // 定期扫描到，该 slot 已经使用了 80% 后需要进行补充
    public static final int SLOTS_COUNT = 1 << 4; // slots 数量

    private long[][] slots;//缓存的 uid
    private AtomicInteger[] startIndex = new AtomicInteger[SLOTS_COUNT];// 可使用的缓存 uid 起始地址
    private AtomicBoolean[] fillingSlots = new AtomicBoolean[SLOTS_COUNT]; // 是否在填充数组

    private BufferPaddingExecutor bufferPaddingExecutor;

    @Getter
    @Autowired
    @Qualifier("UidGenService")
    private UidGenService uidGenService;

    public List<Long> take(int batchSize) {
        int slotsIndexToUse = new Random().nextInt(SLOTS_COUNT);
        while (!fillingSlots[slotsIndexToUse].get() && batchSize <= slots[0].length >> 2) {
            if (batchSize > slots[slotsIndexToUse].length - startIndex[slotsIndexToUse].get()) {
                // 所需要的缓存资源不够
                fillingSlots[slotsIndexToUse].set(true);
                bufferPaddingExecutor.asyncPadding();
                break;
            }
            int calStartIndex = startIndex[slotsIndexToUse].get();
            int calNextIndex = calStartIndex + batchSize;
            if (startIndex[slotsIndexToUse].compareAndSet(calStartIndex, calNextIndex)) {
                // 计算成功
                ArrayList<Long> result = new ArrayList<>(batchSize << 1);
                for (int i = calStartIndex; i < calNextIndex; i++) {
                    result.add(slots[slotsIndexToUse][i]);
                }
                return result;
            }
        }
        return uidGenService.getUidBatch(batchSize);
    }

    /**
     * 每隔 1 分钟进行一次检查
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleFillSlots() {
        int slotsIndex;
        boolean hasSlotsNeedReload = false;
        for (slotsIndex = 0; slotsIndex < SLOTS_COUNT; slotsIndex++) {
            if (!fillingSlots[slotsIndex].get() &&
                    (startIndex[slotsIndex].get() / slots[slotsIndex].length) > DEFAULT_PADDING_PERCENT) {
                // 当前 slot 没有被填充，并且消耗的 uid 超过 80%
                fillingSlots[slotsIndex].set(true);
                hasSlotsNeedReload = true;
            }
        }
        if (hasSlotsNeedReload) {
            bufferPaddingExecutor.asyncPadding();
        }
    }

    @Override
    public void afterPropertiesSet() {
        Assert.isTrue(uidGenService.getDefUidGenService().getUidGenBase().getBitsAllocate().getSequenceBits() + DEFAULT_BOOST_POWER < 32, "catched uid too match, overflow exception");
        int slotSize = 1 << (uidGenService.getDefUidGenService().getUidGenBase().getBitsAllocate().getSequenceBits() + DEFAULT_BOOST_POWER);
        slots = new long[SLOTS_COUNT][slotSize];
        for (int i = 0; i < SLOTS_COUNT; i++) {
            startIndex[i] = new AtomicInteger(0);
            fillingSlots[i] = new AtomicBoolean(false);
        }
        initSlots();
        bufferPaddingExecutor = new BufferPaddingExecutor(this);
    }

    @Override
    public void destroy() {
        bufferPaddingExecutor.shutdown();
    }

    /**
     * 扫描并填充需要填充的 slots
     */
    public synchronized void fillSlots() {
        int slotsIndex;
        for (slotsIndex = 0; slotsIndex < SLOTS_COUNT; slotsIndex++) {
            if (fillingSlots[slotsIndex].get() && startIndex[slotsIndex].get() > 0) {
                List<Long> uids = uidGenService.getUidBatch(startIndex[slotsIndex].get());
                int i = 0;
                for (Long id : uids) {
                    slots[slotsIndex][i++] = id;
                }
                startIndex[slotsIndex].set(0);
                fillingSlots[slotsIndex].set(false);
            }
        }
    }

    /**
     * 对 slots 进行初始化
     */
    private void initSlots() {
        UidGenServiceBase uidGenBase = uidGenService.getDefUidGenService().getUidGenBase();
        long currentTimestamp = System.currentTimeMillis();
        for (int i = 0; i < SLOTS_COUNT; i++) {
            int index = 0;
            for (int j = 0; j < (1 << DEFAULT_BOOST_POWER); j++) {
                List<Long> uids = uidGenBase.getBitsAllocate().generateAllUidAtOneTimestamp(currentTimestamp - uidGenBase.getEpochMilliSeconds(), uidGenBase.getWorkNodeId(), 0L);
                for (Long u : uids) {
                    slots[i][index++] = u;
                }
                uidGenBase.getNextMilliSecond(currentTimestamp);
                currentTimestamp++;
            }
            Assert.isTrue(index == slots[i].length, "init slots failed");
        }

    }
}
