package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmRefundOrder;

public interface TsmRefundOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TsmRefundOrder record);

    int insertSelective(TsmRefundOrder record);

    TsmRefundOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmRefundOrder record);

    int updateByPrimaryKey(TsmRefundOrder record);
}