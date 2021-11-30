package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmOpencardInfo;

import java.util.List;

public interface TsmOpencardInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TsmOpencardInfo record);

    int insertSelective(TsmOpencardInfo record);

    TsmOpencardInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmOpencardInfo record);

    int updateByPrimaryKey(TsmOpencardInfo record);

    // 查询当日的发卡信息
    List<TsmOpencardInfo> selByToday();
}