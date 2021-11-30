package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmOpencardSyncfile;

public interface TsmOpencardSyncfileMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TsmOpencardSyncfile record);

    int insertSelective(TsmOpencardSyncfile record);

    TsmOpencardSyncfile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmOpencardSyncfile record);

    int updateByPrimaryKey(TsmOpencardSyncfile record);
}