package com.heyue.hbcxservice.service.impl;

import cn.com.heyue.entity.TsmCardDetail;
import cn.com.heyue.entity.TsmCardstatusNotify;
import cn.com.heyue.entity.TsmOpencardInfo;
import cn.com.heyue.mapper.TsmCardDetailMapper;
import cn.com.heyue.mapper.TsmCardstatusNotifyMapper;
import com.alibaba.fastjson.JSON;
import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.request.AppStatusNoticeReq;
import com.heyue.hbcxservice.service.TsmCardstatusNotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TsmCardstatusNotifyServiceImpl implements TsmCardstatusNotifyService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass()); // 日志类

    @Autowired
    private TsmCardstatusNotifyMapper tsmCardstatusNotifyMapper;

    @Autowired
    private TsmCardDetailMapper tsmCardDetailMapper;

    @Override
    public Result<String> appStatusNotice(AppStatusNoticeReq req) {
        try {
            TsmCardstatusNotify cardstatusNotify = new TsmCardstatusNotify();
            BeanUtils.copyProperties(req, cardstatusNotify);
            TsmCardDetail cardDetail = tsmCardDetailMapper.selectOneByServiceOrderId(req.getServiceOrderId());
            cardstatusNotify.setCardNo(cardDetail.getCardNo());
            cardstatusNotify.setCreatetime(new Date());
            tsmCardstatusNotifyMapper.insert(cardstatusNotify);
            // 储存发卡信息
            TsmOpencardInfo tsmOpencardInfo = new TsmOpencardInfo();

        } catch (Exception e) {
            logger.error("应用通知失败 ，req={}", JSON.toJSONString(req));
            return Result.fail(null, "应用通知失败");
        }
        return Result.ok();
    }
}
