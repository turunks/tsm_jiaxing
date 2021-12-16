package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmOrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TsmOrderInfoMapper {
    int deleteByPrimaryKey(String serviceOrderId);

    int insert(TsmOrderInfo record);

    int insertSelective(TsmOrderInfo record);

    TsmOrderInfo selectByPrimaryKey(String serviceOrderId);

    /**
     * 查可退款订单列表
     * @param userId
     * @param cardNo
     * @return
     */
    List<TsmOrderInfo> getRefund(@Param("userId") String userId, @Param("cardNo") String cardNo);

    int updateByPrimaryKeySelective(TsmOrderInfo record);

    int updateByPrimaryKey(TsmOrderInfo record);
}