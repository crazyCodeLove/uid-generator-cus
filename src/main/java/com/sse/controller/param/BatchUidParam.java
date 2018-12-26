package com.sse.controller.param;

import com.sse.exception.UidBatchSizeOverflowException;
import com.sse.model.RequestParamBase;
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
public class BatchUidParam extends RequestParamBase {
    private static final int MAX_BATCH_SIZE = 100000; //单次请求最大可以获得 uid 的数量
    private Integer batchSize;

    @Override
    public void validParamInParam() {
        super.validParamInParam();
        if (batchSize == null || batchSize <= 0) {
            batchSize = 1;
        }
        if (batchSize > MAX_BATCH_SIZE) {
            throw new UidBatchSizeOverflowException("batchSize overflow MAX_BATCH_SIZE, try no more than " + MAX_BATCH_SIZE);
        }
    }
}
