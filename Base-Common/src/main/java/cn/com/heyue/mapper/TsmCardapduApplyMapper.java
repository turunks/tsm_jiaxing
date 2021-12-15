package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmCardapduApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TsmCardapduApplyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TsmCardapduApply record);

    int insertSelective(TsmCardapduApply record);

    TsmCardapduApply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmCardapduApply record);

    int updateByPrimaryKey(TsmCardapduApply record);

    int updateByCardNo(TsmCardapduApply record);

    /**
     * 根据卡号和卡操作类型查询卡指令记录
     *
     * @param cardNo
     * @return
     */
    List<TsmCardapduApply> selByCradNo(@Param("cardNo")String cardNo,@Param("card_optype")String card_optype);
}