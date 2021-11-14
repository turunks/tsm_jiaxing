package com.heyue.cityservice.controller;


import cn.com.heyue.entity.TsmTerminalOrder;
import cn.com.heyue.mapper.TsmTerminalOrderMapper;
import cn.com.heyue.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    @RequestMapping("writeFile")
    @ResponseBody
    public void createCard() {
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        String date_2 = df.format(new Date().getTime());
        String path = "D:\\" + date_2 + ".SQ";
        List<String> accountList = new ArrayList<>();
        accountList.add("111,222,333");
        accountList.add("");
        accountList.add("TRADELIST END,000000,000000000000");
        try {
            FileUtils.writeFileContext(accountList, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("readFile")
    @ResponseBody
    public void readFile () {
        try {
            String path = "D:\\" + "211113233614" + ".SQ";
            List<String> list = FileUtils.getFile(path);
            String s = list.toString();
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}