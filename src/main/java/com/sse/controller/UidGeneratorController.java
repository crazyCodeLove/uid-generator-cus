package com.sse.controller;

import com.sse.service.DefaultUidGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pczhao
 * @email
 * @date 2018-12-21 8:39
 */

@RestController
public class UidGeneratorController {

    @Autowired
    private DefaultUidGenService uidGenService;

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public long getUid() {
        return uidGenService.getUid();
    }
}
