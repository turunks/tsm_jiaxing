package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmOrderInfo;

public interface TsmOrderInfoMapper {
    int deleteByPrimaryKey(String serviceOrderId);

    int insert(TsmOrderInfo record);

    int insertSelective(TsmOrderInfo record);

    TsmOrderInfo selectByPrimaryKey(String serviceOrderId);

    int updateByPrimaryKeySelective(TsmOrderInfo record);

    int updateByPrimaryKey(TsmOrderInfo record);
}