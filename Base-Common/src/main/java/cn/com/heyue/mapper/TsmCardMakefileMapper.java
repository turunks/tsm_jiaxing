package cn.com.heyue.mapper;

import cn.com.heyue.entity.TsmCardMakefile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TsmCardMakefileMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TsmCardMakefile record);

    int insertSelective(TsmCardMakefile record);

    TsmCardMakefile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TsmCardMakefile record);

    int updateByPrimaryKey(TsmCardMakefile record);

    // 根据流水号更新反馈回来文件数据
    int updateBySerialno(TsmCardMakefile record);

    // 根据流水号查询制卡数据文件表
    TsmCardMakefile selBySerialno(TsmCardMakefile record);
}