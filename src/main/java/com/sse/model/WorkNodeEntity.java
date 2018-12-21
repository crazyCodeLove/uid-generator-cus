package com.sse.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author pczhao
 * @email
 * @date 2018-12-20 16:06
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkNodeEntity {

    /**
     * Entity unique id (table unique)
     */
    private Long id;

    /**
     * Type of CONTAINER: HostName, ACTUAL : IP.
     */
    private String ip;

    /**
     * Type of CONTAINER: Port, ACTUAL : Timestamp + Random(0-10000)
     */
    private String port;

    /**
     * worke node id, unique
     */
    private Integer workNodeId;

    /**
     * Worker launch date, default now
     */
    private Date launchDate;

    /**
     * server status, on or off
     */
    private String status;

    /**
     * Last access time
     */
    private Date lastUpdateTime;
}
