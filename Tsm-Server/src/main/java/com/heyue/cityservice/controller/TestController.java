package com.heyue.cityservice.controller;


import cn.com.heyue.entity.TsmTerminalOrder;
import cn.com.heyue.mapper.TsmTerminalOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author lyc
 * @date 2020-07-01 17:19:03
 */
@RestController
public class TestController {

    @Autowired
    private TsmTerminalOrderMapper tsmTerminalOrderMapper;


    @RequestMapping("TsmTerminalOrder/get")
    @ResponseBody
    public TsmTerminalOrder getTsmTerminalOrder() {
        TsmTerminalOrder tsmTerminalOrder = tsmTerminalOrderMapper.selectByPrimaryKey((long) 1);
        return tsmTerminalOrder;
    }

}