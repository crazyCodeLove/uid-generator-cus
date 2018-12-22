package com.sse.uid;

import com.sse.model.WorkNodeEntity;

import java.util.List;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-20 22:30
 */

public interface WorkNodeAssigner {

    /** 获取服务所在节点的ID */
    int getWorkNodeId();

    void updateWorkNodeAccessTime(int workNodeId);

    List<WorkNodeEntity> getAllWorkNodeLastUpdateTime();

    void deleteWorkNode(long id);

}
