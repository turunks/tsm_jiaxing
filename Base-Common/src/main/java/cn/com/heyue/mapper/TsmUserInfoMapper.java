package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmUserInfo;

public interface TsmUserInfoMapper {
    int deleteByPrimaryKey(String userId);

    int insert(TsmUserInfo record);

    int insertSelective(TsmUserInfo record);

    TsmUserInfo selectByPrimaryKey(String userId);

    TsmUserInfo selectByThirdUserId(String thirdUserId);

    int updateByPrimaryKeySelective(TsmUserInfo record);

    int updateByPrimaryKey(TsmUserInfo record);
}