package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmPayOrder;
import org.apache.ibatis.annotations.Param;

public interface TsmPayOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TsmPayOrder record);

    int insertSelective(TsmPayOrder record);

    TsmPayOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmPayOrder record);

    int updateByPrimaryKey(TsmPayOrder record);

    /**
     * 根据业务订单号查支付订单
     * @param serviceOrderId 业务订单号
     * @return
     */
    TsmPayOrder selectByServiceOrderId(@Param("serviceOrderId") String serviceOrderId);

}