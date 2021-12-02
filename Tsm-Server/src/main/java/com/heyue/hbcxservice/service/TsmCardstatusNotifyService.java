package com.heyue.hbcxservice.service;

import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.request.AppStatusNoticeReq;

public interface TsmCardstatusNotifyService {

    /**
     * 应用通知
     * @param req
     * @return
     */
    Result<String> appStatusNotice(AppStatusNoticeReq req);
}
