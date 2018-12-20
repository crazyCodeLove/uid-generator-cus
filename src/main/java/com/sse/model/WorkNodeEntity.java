package com.sse.model;

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
@Builder
public class WorkNodeEntity {

    /**
     * Entity unique id (table unique)
     */
    private long id;

    /**
     * Type of CONTAINER: HostName, ACTUAL : IP.
     */
    private String ip;

    /**
     * Type of CONTAINER: Port, ACTUAL : Timestamp + Random(0-10000)
     */
    private String port;

    /**
     * Worker launch date, default now
     */
    private Date launchDate = new Date();

    /**
     * Created time
     */
    private Date created;

    /**
     * Last modified
     */
    private Date modified;
}
