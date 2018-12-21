package com.sse.service;

import com.sse.config.UidGeneratorConfig;
import com.sse.exception.WorkIdOverFlowException;
import com.sse.mapper.WorkNodeMapper;
import com.sse.model.WorkNodeEntity;
import com.sse.uid.BitsAllocate;
import com.sse.uid.WorkNodeAssigner;
import com.sse.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-20 22:43
 */

@Service
public class WorkNodeService implements WorkNodeAssigner {

    @Autowired
    private WorkNodeMapper workNodeMapper;

    @Autowired
    private UidGeneratorConfig uidGeneratorConfig;

    @Autowired
    private BitsAllocate bitsAllocate;


    @Transactional
    @Override
    public int getWorkNodeId() {
        WorkNodeEntity entity = buildWorkerNode();
        if (uidGeneratorConfig.getWorkNodeId() != null) {
            entity.setWorkNodeId(uidGeneratorConfig.getWorkNodeId());
        } else {
            // 如果同一ip 同一 port 会为每一个请求新加一个 workId
            List<Integer> allWorkNodeId = workNodeMapper.getAllWorkNodeId();
            entity.setWorkNodeId(getAvaiableId(allWorkNodeId));
            workNodeMapper.addWorkNode(entity);
        }
        return entity.getWorkNodeId();
    }

    /**
     * work id 生成策略是对最大 WorkerId 求余。
     * 例如 workidbits = 3, id = 15, getAvaiableId 返回 15 % (2^3) = 7
     *
     * @param workNodeIds
     * @return
     */
    private int getAvaiableId(List<Integer> workNodeIds) throws WorkIdOverFlowException {
        Set<Integer> nodeIdSet = new HashSet<>(workNodeIds);
        int max_work_node_id = 1 << bitsAllocate.getWorkIdBits();
        for (int i = 0; i < max_work_node_id; i++) {
            if (!nodeIdSet.contains(i)) {
                return i;
            }
        }
        throw new WorkIdOverFlowException("work id overflow.");
    }

    /**
     * Build worker node entity by IP and PORT
     */
    private WorkNodeEntity buildWorkerNode() {
        WorkNodeEntity workerNodeEntity = new WorkNodeEntity();
        workerNodeEntity.setIp(IpUtil.getLocalIpAddr());
        workerNodeEntity.setPort(uidGeneratorConfig.getServerPort());
        workerNodeEntity.setStatus("on");
        return workerNodeEntity;
    }
}
