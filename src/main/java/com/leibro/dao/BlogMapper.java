package com.leibro.dao;

import com.leibro.model.Blog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BlogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Blog record);

    int insertSelective(Blog record);

    Blog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKeyWithBLOBs(Blog record);

    int updateByPrimaryKey(Blog record);

    Blog selectByUri(String uri);

    List<Blog> selectByYearAndMonthOrderByDay(@Param("year") int year,@Param("month") int month);

    List<Blog> selectAllOrderByCreateTimeDesc();

    List<Blog> selectAllByKeyword(String keyword);

    List<Blog> selectAllByTag(String tag);
}