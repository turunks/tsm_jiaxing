package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmCardConsumedetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TsmCardConsumedetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TsmCardConsumedetail record);

    int insertSelective(TsmCardConsumedetail record);

    TsmCardConsumedetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmCardConsumedetail record);

    int updateByPrimaryKey(TsmCardConsumedetail record);

    int insertBatch(List<TsmCardConsumedetail> list);
}