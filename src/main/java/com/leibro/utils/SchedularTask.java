package com.leibro.utils;

import com.leibro.dao.BlogMapper;
import com.leibro.model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Set;

/**
 * Created by leibro on 2017/1/30.
 */
public class SchedularTask {
    @Autowired
    BlogMapper blogMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Scheduled(cron = "0 0/1 * * * *")
    public void calcCount() {
        ZSetOperations operations = redisTemplate.opsForZSet();
        Set<Integer> entries = operations.range("count",0,-1);
        for(int id:entries) {
            double score = operations.score("count",id);
            Blog blog = blogMapper.selectByPrimaryKey(id);
            blog.setReadCount(blog.getReadCount() + (int)score);
            blogMapper.updateByPrimaryKeySelective(blog);
        }
        redisTemplate.delete("count");
        List<Blog> blogs = blogMapper.selectAllOrderByCreateTimeDesc();
        for(Blog blog:blogs) {
            operations.add("count",blog.getId(),0);
        }
    }
}
