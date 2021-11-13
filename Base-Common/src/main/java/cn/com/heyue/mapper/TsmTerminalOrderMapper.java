package cn.com.heyue.mapper;


import cn.com.heyue.entity.TsmTerminalOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TsmTerminalOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TsmTerminalOrder record);

    int insertSelective(TsmTerminalOrder record);

    TsmTerminalOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmTerminalOrder record);

    int updateByPrimaryKey(TsmTerminalOrder record);
}