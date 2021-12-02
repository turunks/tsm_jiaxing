package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmTerminal;
import org.apache.ibatis.annotations.Param;

public interface TsmTerminalMpper {
    int deleteByPrimaryKey(Long id);

    int insert(TsmTerminal record);

    int insertSelective(TsmTerminal record);

    TsmTerminal selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmTerminal record);

    int updateByPrimaryKey(TsmTerminal record);

    /**
     * 根据城市代码查终端机
     * @param cityCode
     * @return
     */
    TsmTerminal selectByCityCode(@Param("cityCode") String cityCode);
}