package com.leibro.service;

import com.github.pagehelper.PageHelper;
import com.leibro.dao.BlogDao;
import com.leibro.dao.BlogMapper;
import com.leibro.dao.TagMapper;
import com.leibro.dao.UserMapper;
import com.leibro.model.Blog;
import com.leibro.model.Tag;
import com.leibro.model.User;
import com.leibro.utils.BlogAbstractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.PostConstruct;
import java.time.Month;
import java.util.*;

/**
 * Created by leibro on 2017/1/6.
 */
@Service
public class VisitService {
//    @Autowired
//    BlogMapper blogMapper;

    @Autowired
    BlogDao blogDao;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    TagMapper tagMapper;


    public void visitBlog(String uri,Model model) {
        Blog blog = blogDao.selectByUri(uri);
        User user = userMapper.selectByPrimaryKey(blog.getAuthor());
        blogDao.addViewCount(blog.getId());
        List<Tag> tags = tagMapper.selectByBlogId(blog.getId());
        int cacheCount = blogDao.getBlogCacheCount(blog.getId());
        model.addAttribute("count",cacheCount + blog.getReadCount());
        model.addAttribute("blog",blog);
        model.addAttribute("tags",tags);
        model.addAttribute("author",user);
    }

    public void blogYearArchive(int year,Model model) {
        Map<String,List<Blog>> allMonthBlogs = new LinkedHashMap<>();
        for(int i = 1;i <= 12;i ++) {
            List<Blog> blogs = blogDao.selectByYearAndMonthOrderByDay(year,i);
            allMonthBlogs.put(Month.of(i).toString(),blogs);
        }
        model.addAttribute("allMonthBlogs",allMonthBlogs);
        model.addAttribute("year",year);
    }

    public void getBlogsForHomeByOffset(Model model,int offset) {
        PageHelper.startPage(offset + 1,6);
        List<Blog> blogs = blogMapper.selectAllOrderByCreateTimeDesc();
        Map imgUrls = new HashMap();
        Map<Integer,List<Tag>> tagsOfBlog = new HashMap();
        Map abstractContents = new HashMap();
        for(Blog blog:blogs) {
            imgUrls.put(blog.getId(), BlogAbstractor.abstractImgUrl(blog.getContent()));
            abstractContents.put(blog.getId(),BlogAbstractor.abstractContent(blog.getContent()));
            List<Tag> tags = tagMapper.selectByBlogId(blog.getId());
            tagsOfBlog.put(blog.getId(),tags);
        }
        model.addAttribute("blogs",blogs);
        model.addAttribute("imgUrls",imgUrls);
        model.addAttribute("abstractContents",abstractContents);
        model.addAttribute("tags",tagsOfBlog);
    }

    public void searchBlogsByKeyword(String keyword,Model model) {
        List<Blog> blogs = blogDao.selectAllByKeyword(keyword);
        Map abstractContents = new HashMap();
        for(Blog blog:blogs) {
            abstractContents.put(blog.getId(),BlogAbstractor.abstractContent(blog.getContent()));
        }
        model.addAttribute("blogs",blogs);
        model.addAttribute("keyword",keyword);
        model.addAttribute("abstractContents",abstractContents);
    }

    public void searchBlogsByTag(String tag,Model model) {
        List<Blog> blogs = blogDao.selectAllByTag(tag);
        Map abstractContents = new HashMap();
        for(Blog blog:blogs) {
            abstractContents.put(blog.getId(),BlogAbstractor.abstractContent(blog.getContent()));
        }
        model.addAttribute("blogs",blogs);
        model.addAttribute("keyword",tag);
        model.addAttribute("abstractContents",abstractContents);
    }

    public void getHotestBlogs(Model model) {
        List<Blog> hotestBlogs = blogDao.selectHotestBlog();
        model.addAttribute("hotest",hotestBlogs);
    }

    public void getHotestTags(Model model) {
        List<Tag> hotestTags = tagMapper.selectByFreq();
        model.addAttribute("hotestTags",hotestTags);
    }

//    @Cacheable(cacheNames = "blog",key = "#id")
//    public Blog visitBlog(int id) {
//        System.out.println("缓存未命中………………");
//        return blogMapper.selectByPrimaryKey(id);
//    }

}
