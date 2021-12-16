package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmRefundBill;

public interface TsmRefundBillMapper {
    int deleteByPrimaryKey(Long billId);

    int insert(TsmRefundBill record);

    int insertSelective(TsmRefundBill record);

    TsmRefundBill selectByPrimaryKey(Long billId);

    int updateByPrimaryKeySelective(TsmRefundBill record);

    int updateByPrimaryKey(TsmRefundBill record);
}