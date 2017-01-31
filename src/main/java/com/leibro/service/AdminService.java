package com.leibro.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.leibro.dao.*;
import com.leibro.model.Blog;
import com.leibro.model.BlogTagLinkKey;
import com.leibro.model.Tag;
import com.leibro.model.User;
import com.leibro.model.wrapper.BlogAndTagsWrapper;
import com.leibro.utils.BlogAbstractor;
import com.leibro.utils.ChnCharToPinyin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leibro on 2017/1/2.
 */
@Service
public class AdminService {
    @Autowired
    BlogDao blogDao;

    @Autowired
    BlogTagLinkMapper blogTagLinkMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    TagMapper tagMapper;

    public String postBlog(BlogAndTagsWrapper wrapper, Principal principal) {
        User user = userMapper.selectByUsername(principal.getName());
        Blog blog = wrapper.getBlog();
        List<Tag> tags = wrapper.getTags();
        String uri = ChnCharToPinyin.toPinyin(blog.getTitle());
        blog.setLastModify(new Date());
        blog.setCreateTime(new Date());
        blog.setAuthor(user.getId());
        blog.setReadCount(0);
        Blog blog1 = blogDao.selectByUri(uri);
        if(blog1 == null) {
            blog.setUri(uri);
            blogDao.insertSelective(blog);
        } else {
            blogDao.insertSelective(blog);
            uri = uri + "-" + blog.getId();
            blog.setUri(uri);
            blogDao.updateByPrimaryKeySelective(blog);
        }
        for(Tag tag:tags) {
            Tag tag1 = tagMapper.selectByTag(tag.getTag());
            if(tag1 == null) {
                tagMapper.insert(tag);
                BlogTagLinkKey blogTagLinkKey = new BlogTagLinkKey();
                blogTagLinkKey.setBlogId(blog.getId());
                blogTagLinkKey.setTagId(tag.getId());
                blogTagLinkMapper.insert(blogTagLinkKey);
            } else {
                BlogTagLinkKey blogTagLinkKey = new BlogTagLinkKey();
                blogTagLinkKey.setBlogId(blog.getId());
                blogTagLinkKey.setTagId(tag1.getId());
                blogTagLinkMapper.insert(blogTagLinkKey);
            }
        }
        Gson gson = new Gson();
        Map returnJSon = new HashMap();
        returnJSon.put("redirect","/blogs/" + uri);
        return gson.toJson(returnJSon);
    }

    public String updateBlog(BlogAndTagsWrapper blogAndTagsWrapper,Principal principal) {
        Blog blog = blogAndTagsWrapper.getBlog();
        blog.setUri(ChnCharToPinyin.toPinyin(blog.getTitle()));
        blog.setLastModify(new Date());
        blogDao.updateByPrimaryKeySelective(blog);
        List<Tag> oldTags = tagMapper.selectByBlogId(blog.getId());
        for(Tag tag:oldTags) {
            BlogTagLinkKey blogTagLinkKey = new BlogTagLinkKey();
            blogTagLinkKey.setTagId(tag.getId());
            blogTagLinkKey.setBlogId(blog.getId());
            blogTagLinkMapper.deleteByPrimaryKey(blogTagLinkKey);
        }
        List<Tag> newTags = blogAndTagsWrapper.getTags();
        for(Tag tag:newTags) {
            if(tagMapper.selectByTag(tag.getTag()) == null) {
                tagMapper.insert(tag);
            }
            BlogTagLinkKey blogTagLinkKey = new BlogTagLinkKey();
            blogTagLinkKey.setBlogId(blog.getId());
            blogTagLinkKey.setTagId(tag.getId());
            blogTagLinkMapper.insert(blogTagLinkKey);
        }
        Gson gson = new Gson();
        Map returnJSon = new HashMap();
        returnJSon.put("redirect","/blogs/" + blog.getUri());
        return gson.toJson(returnJSon);
    }

    public void adminBlogArchive(int page,Model model) {
        PageHelper.startPage(page,6);
        List<Blog> blogs = blogDao.selectAllOrderByCreateTimeDesc();
        PageInfo pageInfo = new PageInfo(blogs);
        Map<Integer ,String> abstractContentMap = new HashMap<>();
        for (Blog blog:blogs) {
            abstractContentMap.put(blog.getId(), BlogAbstractor.abstractContent(blog.getContent()));
        }
        model.addAttribute("blogs",blogs);
        model.addAttribute("abstractContentMap",abstractContentMap);
        model.addAttribute("pageSum",pageInfo.getPages());
        model.addAttribute("currentPage",page);
    }

    public void editBlog(int blog_id,Model model) {
        Blog blog = blogDao.selectByPrimaryKey(blog_id);
        List<Tag> tags = tagMapper.selectByBlogId(blog_id);
        model.addAttribute("edit_mode",true);
        Gson gson = new Gson();
        model.addAttribute("tags",gson.toJson(tags));
        model.addAttribute("blog",blog);
    }

    public void deleteBlog(int blog_id,Principal principal) {
        blogTagLinkMapper.deleteByBlogId(blog_id);
        blogDao.deleteByPrimaryKey(blog_id);
    }
}
