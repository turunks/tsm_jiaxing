package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmCardstatusNotify;
import org.apache.ibatis.annotations.Param;

public interface TsmCardstatusNotifyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TsmCardstatusNotify record);

    int insertSelective(TsmCardstatusNotify record);

    TsmCardstatusNotify selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmCardstatusNotify record);

    int updateByPrimaryKey(TsmCardstatusNotify record);

    TsmCardstatusNotify selectByCardNo(@Param("cardNo") String cardNo);
}