package com.sse.config;

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

    /** 时间序列所占bit 长度 */
    @Value("${uid.generate.timestampBits}")
    private Integer timestampBits;

    /** workId 所占 bit 长度 */
    @Value("${uid.generate.workIdBits}")
    private Integer workIdBits;

    /** 序列号所占 bit 长度 */
    @Value("${uid.generate.sequenceBits}")
    private Integer sequenceBits;

    @Value("${uid.generate.epochStr}")
    private String epochStr;

    /** 服务器节点编号 */
    @Value("${uid.workNode.nodeId}")
    private Integer workNodeId;

    @Value("${server.port}")
    private String serverPort;

    /** 分配各部分所占位长度 */
    @Bean
    public BitsAllocate bitsAllocate() {
        BitsAllocate bitsAllocate = new BitsAllocate(timestampBits, workIdBits, sequenceBits);
        return bitsAllocate;
    }







}
