package cn.com.heyue.mapper;


import cn.com.heyue.entity.TsmCardDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TsmCardDetailMapper  {

    int deleteByPrimaryKey(Long id);

    int insert(TsmCardDetail record);

    int insertSelective(TsmCardDetail record);

    TsmCardDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmCardDetail record);

    int updateByPrimaryKey(TsmCardDetail record);
}