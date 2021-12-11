package cn.com.heyue.mapper;


import cn.com.heyue.entity.TsmCardDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TsmCardDetailMapper  {

    int deleteByPrimaryKey(Long id);

    int insert(TsmCardDetail record);

    int insertSelective(TsmCardDetail record);

    TsmCardDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmCardDetail record);

    int updateByPrimaryKey(TsmCardDetail record);

    /**
     * 根据业务订单号取已关联的卡数据
     * @param serviceOrderId
     * @return
     */
    TsmCardDetail selectOneByServiceOrderId(@Param("serviceOrderId") String serviceOrderId);

    /**
     * 获取一条未使用卡数据
     * @param cityCode
     * @param areaCode
     * @return
     */
    TsmCardDetail selectOne(@Param("cityCode") String cityCode, @Param("areaCode") String areaCode,
                            @Param("cardSpecies") String cardSpecies);
}