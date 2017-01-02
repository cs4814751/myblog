package com.leibro.dao;

import com.leibro.model.Tag;
import org.springframework.stereotype.Repository;

@Repository
public interface TagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tag record);

    int insertSelective(Tag record);

    Tag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);
}