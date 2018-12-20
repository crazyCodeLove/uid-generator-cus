package com.sse.service;

import com.sse.config.UidGeneratorConfig;
import com.sse.dao.WorkNodeMapper;
import com.sse.model.WorkNodeEntity;
import com.sse.uid.WorkNodeAssigner;
import com.sse.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    @Override
    public long getWorkNodeId() {
        if (uidGeneratorConfig.getServerNodeId() != null) {
            return uidGeneratorConfig.getServerNodeId();
        }
        WorkNodeEntity entity = buildWorkerNode();
        // add worker node for new (ignore the same IP + PORT)
        workNodeMapper.addWorkerNode(entity);
        return entity.getId();
    }


    /**
     * Build worker node entity by IP and PORT
     */
    private WorkNodeEntity buildWorkerNode() {
        WorkNodeEntity workerNodeEntity = new WorkNodeEntity();
        workerNodeEntity.setIp(IpUtil.getLocalIpAddr());
        workerNodeEntity.setPort(uidGeneratorConfig.getServerPort());
        return workerNodeEntity;
    }
}
