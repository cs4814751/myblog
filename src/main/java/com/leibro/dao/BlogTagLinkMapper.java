package com.leibro.dao;

import com.leibro.model.BlogTagLinkKey;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogTagLinkMapper {
    int deleteByPrimaryKey(BlogTagLinkKey key);

    int insert(BlogTagLinkKey record);

    int insertSelective(BlogTagLinkKey record);

    int deleteByBlogId(int blog_id);
}