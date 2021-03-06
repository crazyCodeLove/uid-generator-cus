package com.sse.mapper;

import com.sse.model.WorkNodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pczhao
 * @email
 * @date 2018-12-20 16:03
 */

@Mapper
public interface WorkNodeMapper {
    void addWorkNode(WorkNodeEntity workerNodeEntity);

    void deleteWorkNode(long id);


    void updateWorkNodeAccessTime(int workNodeId);

    /**
     * 根据 id 获取 WorkNode
     */
    WorkNodeEntity getWorkNode(@Param("id") long id);

    WorkNodeEntity getWorkNodeByWorkNodeId(@Param("workNodeId") int workNodeId);

    List<Integer> getAllWorkNodeId();

    List<WorkNodeEntity> getAllWorkNodeLastUpdateTime();


}
