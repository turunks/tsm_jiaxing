package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmCmpayConfig;

public interface TsmCmpayConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TsmCmpayConfig record);

    int insertSelective(TsmCmpayConfig record);

    TsmCmpayConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TsmCmpayConfig record);

    int updateByPrimaryKey(TsmCmpayConfig record);

    /**
     * 根据申请城市代码查支付参数
     * @param applyCityCode
     * @return
     */
    TsmCmpayConfig selectByApplyCityCode(String applyCityCode);
}