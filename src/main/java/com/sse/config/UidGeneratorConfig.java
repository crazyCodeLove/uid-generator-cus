package com.sse.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${uid.generate.workerIdBits}")
    private Integer workerIdBits;

    /** 序列号所占 bit 长度 */
    @Value("${uid.generate.sequenceBits}")
    private Integer sequenceBits;

    /** 服务器节点编号 */
    @Value("${uid.server-node.node-id}")
    private Integer serverNodeId;

    @Value("${server.port}")
    private String serverPort;







}
