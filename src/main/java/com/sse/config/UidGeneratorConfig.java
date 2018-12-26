package com.sse.config;

import com.sse.exception.UidBitAllocateException;
import com.sse.uid.BitsAllocate;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-20 21:07
 */

@Configuration
@Data
public class UidGeneratorConfig {

    /**
     * 时间序列所占bit 长度
     */
    @Value("${uid.generate.timestampBits}")
    private int timestampBits;

    /**
     * workId 所占 bit 长度
     */
    @Value("${uid.generate.workIdBits}")
    private int workIdBits;

    /**
     * 序列号所占 bit 长度
     */
    @Value("${uid.generate.sequenceBits}")
    private int sequenceBits;

    @Value("${uid.generate.epochStr}")
    private String epochStr;

    /**
     * 服务器节点编号
     */
    @Value("${uid.workNode.nodeId}")
    private Integer workNodeId;

    @Value("${server.port}")
    private String serverPort;

    /**
     * 生成 uid 批处理方法，每次最多可以生成的数量所占位数，每次最多为 131072 个
     */
    private int UID_BATCH_MAX_SIZE = 1 << 17;

    /**
     * 如果 workNodeId last_update_time 超过 120 分钟没有更新就进行删除
     */
    private int INVALID_WORK_NODE_MAX_LAST_TIME_MINUTE = 120;

    /**
     * 分配各部分所占位长度
     */
    @Bean
    public BitsAllocate bitsAllocate() {
        if (timestampBits <= 0 || workIdBits <= 0 || sequenceBits <= 0) {
            throw new UidBitAllocateException("allocating uid bit contains non positive number");
        }
        BitsAllocate bitsAllocate = new BitsAllocate(timestampBits, workIdBits, sequenceBits);
        return bitsAllocate;
    }


}
