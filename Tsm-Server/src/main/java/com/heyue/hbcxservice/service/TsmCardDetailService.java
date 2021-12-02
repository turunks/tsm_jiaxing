package com.heyue.hbcxservice.service;

import com.heyue.bean.Result;
import com.heyue.hbcxservice.message.request.*;
import com.heyue.hbcxservice.message.response.*;

public interface TsmCardDetailService {

    /**
     * 获取开卡数据
     * @param req
     * @return
     */
    Result<GetOpenCardFileRes> getOpenCardFile(GetOpenCardFileReq req);

    /**
     * 圈存申请
     * @param req
     * @return
     */
    Result<TransferApplyRes> transferApply(TransferApplyReq req);

    /**
     * 圈存提交
     * @param req
     * @return
     */
    Result<TransferSubmitRes> transferSubmit(TransferSubmitReq req);

    /**
     * 激活申请
     * @param req
     * @return
     */
    Result<UserCardActiveRes> userCardActive(UserCardActiveReq req);

    /**
     * 激活提交
     * @param req
     * @return
     */
    Result<UserCardActiveSubmitRes> userCardActiveSubmit(UserCardActiveSubmitReq req);
}
