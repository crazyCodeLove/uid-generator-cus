package com.sse.controller.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pczhao
 * @email
 * @date 2018-12-25 15:50
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchUidParam {
    private Integer batchSize;
}
