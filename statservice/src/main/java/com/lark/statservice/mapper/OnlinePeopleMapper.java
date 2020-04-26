package com.lark.statservice.mapper;

import com.lark.statservice.pojo.OnlinePeople;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2020-04-24
 */
@Repository
public interface OnlinePeopleMapper extends BaseMapper<OnlinePeople> {

    List<OnlinePeople> selectOnlinePeopleBatch(@Param("queryTime") String queryTime);
//    List<OnlinePeople> selectOnlinePeopleBatch();
}
