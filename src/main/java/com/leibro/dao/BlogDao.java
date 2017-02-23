package com.leibro.dao;

import com.leibro.model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by leibro on 2017/1/29.
 */
@Repository
public class BlogDao {
    @Autowired
    BlogMapper blogMapper;

    @Autowired
    RedisTemplate template;

    @PostConstruct
    public void initCount() {
        ZSetOperations operations = template.opsForZSet();
        Set<Integer> entries = operations.range("count",0,-1);
        if(entries.size() > 0) {
            for (int id : entries) {
                double score = operations.score("count", id);
                Blog blog = blogMapper.selectByPrimaryKey(id);
                blog.setReadCount(blog.getReadCount() + (int)score);
                blogMapper.updateByPrimaryKeySelective(blog);
            }
        }
        template.getConnectionFactory().getConnection().flushAll();
        List<Blog> blogs = blogMapper.selectAllOrderByCreateTimeDesc();
        for(Blog blog:blogs) {
            operations.add("count",blog.getId(),0);
        }
    }

    public Blog selectByPrimaryKey(int id) {
        return blogMapper.selectByPrimaryKey(id);
    }

    public void addViewCount(int id) {
        ZSetOperations operations = template.opsForZSet();
        operations.incrementScore("count",id,1);
    }

    public int getBlogCacheCount(int id) {
        ZSetOperations operations = template.opsForZSet();
        double count = operations.score("count",id);
        return (int)count;
    }

    @Caching(evict = {@CacheEvict(cacheNames = "blog",allEntries = true),@CacheEvict(cacheNames = "blogs",allEntries = true)})
    public int deleteByPrimaryKey(Integer id) {
        ZSetOperations operations = template.opsForZSet();
        operations.remove("count",id);
        return blogMapper.deleteByPrimaryKey(id);
    }


    @Caching(evict = {@CacheEvict(cacheNames = "blogs",allEntries = true),@CacheEvict(cacheNames = "blog",allEntries = true)})
    public int insert(Blog record) {
         return blogMapper.insert(record);
    }
    @Caching(evict = {@CacheEvict(cacheNames = "blogs",allEntries = true),@CacheEvict(cacheNames = "blog",allEntries = true)})
    public int insertSelective(Blog record) {
         return blogMapper.insertSelective(record);

    }

    @Caching(evict = {@CacheEvict(cacheNames = "blogs",allEntries = true),@CacheEvict(cacheNames = "blog",allEntries = true)})
    public Blog updateByPrimaryKeySelective(Blog record) {
        blogMapper.updateByPrimaryKeySelective(record);
        return record;
    }

    @Caching(evict = {@CacheEvict(cacheNames = "blogs",allEntries = true),@CacheEvict(cacheNames = "blog",allEntries = true)})
    public int updateByPrimaryKeyWithBLOBs(Blog record) {
        return blogMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Caching(evict = {@CacheEvict(cacheNames = "blogs",allEntries = true),@CacheEvict(cacheNames = "blog",allEntries = true)})
    public int updateByPrimaryKey(Blog record) {
        return blogMapper.updateByPrimaryKey(record);
    }

    @Cacheable(cacheNames = "blog",key = "#uri")
    public Blog selectByUri(String uri) {
        return blogMapper.selectByUri(uri);
    }

    @Cacheable(cacheNames = "blogs",key = "#year + '-' + #month" )
    public List<Blog> selectByYearAndMonthOrderByDay(int year, int month) {
        return blogMapper.selectByYearAndMonthOrderByDay(year,month);
    }

    @Cacheable(cacheNames = "blogs",key = "'AllDesc'")
    public List<Blog> selectAllOrderByCreateTimeDesc() {
        return blogMapper.selectAllOrderByCreateTimeDesc();
    }

    @Cacheable(cacheNames = "blogs",key = "'AllAsc'")
    public List<Blog> selectAllOrderByCreateTimeAsc() {
        return blogMapper.selectAllOrderByCreateTimeAsc();
    }

    @Cacheable(cacheNames = "blogs",key = "#keyword")
    public List<Blog> selectAllByKeyword(String keyword) {
        return blogMapper.selectAllByKeyword(keyword);
    }

    @Cacheable(cacheNames = "blogs",key = "#tag")
    public List<Blog> selectAllByTag(String tag) {
        return blogMapper.selectAllByTag(tag);
    }

    public List<Blog> selectHotestBlog() {
        List<Blog> hotestBlogs = new ArrayList<>();
        ZSetOperations operations = template.opsForZSet();
        Set<Integer> entries = operations.reverseRange("count",0,4);
        for(int id:entries) {
            hotestBlogs.add(blogMapper.selectByPrimaryKey(id));
        }
        return hotestBlogs;
    }
}
