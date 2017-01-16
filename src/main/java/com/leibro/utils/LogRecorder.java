package com.leibro.utils;

import com.leibro.dao.UserMapper;
import com.leibro.model.User;
import com.leibro.model.wrapper.BlogAndTagsWrapper;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;


/**
 * Created by leibro on 2017/1/15.
 */
@Aspect
public class LogRecorder {
    Logger logger = Logger.getLogger(LogRecorder.class);

    @Autowired
    UserMapper userMapper;

    @Before("execution(* com.leibro.service.AdminService.postBlog(..)) && args(blogAndTagsWrapper,principal))")
    public void tryPostBlog(BlogAndTagsWrapper blogAndTagsWrapper, Principal principal) {
        User user = userMapper.selectByPrimaryKey(blogAndTagsWrapper.getBlog().getAuthor());
        logger.info("User:" + principal.getName() + " try to post blog:" + blogAndTagsWrapper.getBlog().getTitle());
    }


    @AfterReturning("execution(* com.leibro.service.AdminService.postBlog(..))")
    public void postSuccess() {
        logger.info("Blog Post Success!");
    }

    @Before("execution(* com.leibro.service.AdminService.updateBlog(..)) && args(blogAndTagsWrapper,principal))")
    public void tryEditBlog(BlogAndTagsWrapper blogAndTagsWrapper,Principal principal) {
        logger.info("User:" + principal.getName() + " try to edit blog id:" + blogAndTagsWrapper.getBlog().getId() + " title:" + blogAndTagsWrapper.getBlog().getTitle());
    }

    @AfterReturning("execution(* com.leibro.service.AdminService.updateBlog(..))")
    public void editSuccess() {
        logger.info("Blog Edit Success!");
    }

    @Before("execution(* com.leibro.service.AdminService.deleteBlog(..)) && args(blog_id,principal)")
    public void deleteBlog(int blog_id,Principal principal) {
        logger.info("User:" + principal.getName() + " delete blog id:" + blog_id);
    }
}
