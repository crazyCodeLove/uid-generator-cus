package com.sse.service.catche;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author pczhao
 * @email
 * @date 2018-12-25 11:10
 */

public class BufferPaddingExecutor {
    private RingBuffer ringBuffer;
    /**
     * Padding immediately by the thread pool
     */
    private final ExecutorService bufferPadExecutors;

    public BufferPaddingExecutor(RingBuffer ringBuffer) {
        this.ringBuffer = ringBuffer;
        int cores = Runtime.getRuntime().availableProcessors();
        cores = cores > 1 ? cores : 1;
        this.bufferPadExecutors = Executors.newFixedThreadPool(cores);
    }

    /**
     * Shutdown executors
     */
    public void shutdown() {
        if (!bufferPadExecutors.isShutdown()) {
            bufferPadExecutors.shutdownNow();
        }
    }

    /**
     * Padding buffer in the thread pool
     */
    public void asyncPadding() {
        bufferPadExecutors.submit(ringBuffer::fillSlots);
    }


}
