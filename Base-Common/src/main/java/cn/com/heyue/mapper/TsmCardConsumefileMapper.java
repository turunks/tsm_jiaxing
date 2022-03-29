package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmCardConsumefile;

public interface TsmCardConsumefileMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TsmCardConsumefile record);

    int insertSelective(TsmCardConsumefile record);

    TsmCardConsumefile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmCardConsumefile record);

    int updateByPrimaryKey(TsmCardConsumefile record);

    TsmCardConsumefile selectByfilename(TsmCardConsumefile record);
}