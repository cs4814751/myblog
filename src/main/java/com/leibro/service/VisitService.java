package com.leibro.service;

import com.github.pagehelper.PageHelper;
import com.leibro.dao.BlogMapper;
import com.leibro.dao.TagMapper;
import com.leibro.dao.UserMapper;
import com.leibro.model.Blog;
import com.leibro.model.Tag;
import com.leibro.model.User;
import com.leibro.utils.BlogAbstractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.Month;
import java.util.*;

/**
 * Created by leibro on 2017/1/6.
 */
@Service
public class VisitService {
    @Autowired
    BlogMapper blogMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    TagMapper tagMapper;

    public void visitBlog(String uri,Model model) {
        Blog blog = blogMapper.selectByUri(uri);
        User user = userMapper.selectByPrimaryKey(blog.getAuthor());
        int readCount = blog.getReadCount();
        blog.setReadCount(++ readCount);
        blogMapper.updateByPrimaryKeySelective(blog);
        List<Tag> tags = tagMapper.selectByBlogId(blog.getId());
        model.addAttribute("blog",blog);
        model.addAttribute("tags",tags);
        model.addAttribute("author",user);
    }

    public void blogYearArchive(int year,Model model) {
        Map<String,List<Blog>> allMonthBlogs = new LinkedHashMap<>();
        for(int i = 1;i <= 12;i ++) {
            List<Blog> blogs = blogMapper.selectByYearAndMonthOrderByDay(year,i);
            allMonthBlogs.put(Month.of(i).toString(),blogs);
        }
        model.addAttribute("allMonthBlogs",allMonthBlogs);
        model.addAttribute("year",year);
    }

    public void getBlogsForHomeByOffset(Model model,int offset) {
        PageHelper.startPage(offset + 1,6);
        List<Blog> blogs = blogMapper.selectAllOrderByCreateTimeDesc();
        Map imgUrls = new HashMap();
        Map abstractContents = new HashMap();
        for(Blog blog:blogs) {
            imgUrls.put(blog.getId(), BlogAbstractor.abstractImgUrl(blog.getContent()));
            abstractContents.put(blog.getId(),BlogAbstractor.abstractContent(blog.getContent()));
        }
        model.addAttribute("blogs",blogs);
        model.addAttribute("imgUrls",imgUrls);
        model.addAttribute("abstractContents",abstractContents);
    }

    public void searchBlogsByKeyword(String keyword,Model model) {
        List<Blog> blogs = blogMapper.selectAllByKeyword(keyword);
        Map abstractContents = new HashMap();
        for(Blog blog:blogs) {
            abstractContents.put(blog.getId(),BlogAbstractor.abstractContent(blog.getContent()));
        }
        model.addAttribute("blogs",blogs);
        model.addAttribute("keyword",keyword);
        model.addAttribute("abstractContents",abstractContents);
    }

    public void searchBlogsByTag(String tag,Model model) {
        List<Blog> blogs = blogMapper.selectAllByTag(tag);
        Map abstractContents = new HashMap();
        for(Blog blog:blogs) {
            abstractContents.put(blog.getId(),BlogAbstractor.abstractContent(blog.getContent()));
        }
        model.addAttribute("blogs",blogs);
        model.addAttribute("keyword",tag);
        model.addAttribute("abstractContents",abstractContents);
    }

}
