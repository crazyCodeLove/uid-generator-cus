package com.sse.dao;

import com.sse.model.WorkNodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author pczhao
 * @email
 * @date 2018-12-20 16:03
 */

@Mapper
public interface WorkNodeMapper {

    WorkNodeEntity getWorkerNodeByHostPort(@Param("ip") String ip, @Param("port") String port);

    void addWorkerNode(WorkNodeEntity workerNodeEntity);

}
