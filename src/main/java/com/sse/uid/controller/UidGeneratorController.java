package com.sse.uid.controller;

import com.sse.uid.controller.param.BatchUidParam;
import com.sse.model.RequestParamHolder;
import com.sse.model.ResponseResultHolder;
import com.sse.uid.service.CachedUidGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author pczhao
 * @email
 * @date 2018-12-21 8:39
 */

@RestController
public class UidGeneratorController {

    @Autowired
    private CachedUidGenService uidGenService;

    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    public ResponseResultHolder<List<Long>> getUidBatch(@RequestBody RequestParamHolder<BatchUidParam> uidBatchParam) {
        if (uidBatchParam.getParam() == null) {
            uidBatchParam = new RequestParamHolder<>();
            uidBatchParam.setParam(BatchUidParam.builder().batchSize(1).build());
        }
        ResponseResultHolder<List<Long>> result = new ResponseResultHolder<>();
        List<Long> uids = uidGenService.getUidBatch(uidBatchParam.getParam().getBatchSize());
        result.setResult(uids);
        return result;
    }
}
