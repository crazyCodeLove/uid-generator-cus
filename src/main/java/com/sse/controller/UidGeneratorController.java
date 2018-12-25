package com.sse.controller;

import com.sse.controller.param.BatchUidParam;
import com.sse.exception.RTException;
import com.sse.service.CachedUidGenService;
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

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public long getUid() {
        return uidGenService.getUid();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    public List<Long> getUidBatch(@RequestBody BatchUidParam param) {
        if (param == null) {
            param = BatchUidParam.builder().batchSize(1).build();
        }
        if (param.getBatchSize() <= 0) {
            param.setBatchSize(1);
        }
        if (param.getBatchSize() > 100000) {
            throw new RTException("batch size over flow, try less than 100000");
        }
        return uidGenService.getUidBatch(param.getBatchSize());
    }
}
