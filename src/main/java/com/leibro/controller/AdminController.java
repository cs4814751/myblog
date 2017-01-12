package com.leibro.controller;

import com.leibro.service.AdminService;
import com.leibro.model.wrapper.BlogAndTagsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by leibro on 2017/1/2.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;




    @RequestMapping("/blog/new")
    public String newBlog() {
        return "blog-new";
    }

    @RequestMapping("/blog/edit/{blog_id}")
    public String editBlog(@PathVariable("blog_id") int blog_id,Model model) {
        adminService.editBlog(blog_id,model);
        return "blog-edit";
    }

    @ResponseBody
    @RequestMapping("/blog/post")
    public String postBlog(@RequestBody BlogAndTagsWrapper wrapper) {
        return adminService.postBlog(wrapper);
    }

    @ResponseBody
    @RequestMapping("/blog/update")
    public String updateBlog(@RequestBody BlogAndTagsWrapper wrapper) {
        return adminService.updateBlog(wrapper);
    }

    @RequestMapping("/archive/{page}")
    public String archieve(@PathVariable("page") int page,Model model) {
        adminService.adminBlogArchive(page,model);
        return "admin-archive";
    }

    @RequestMapping("/blog/delete/{blog_id}")
    public String deleteBlog(@PathVariable("blog_id") int blog_id) {
        adminService.deleteBlog(blog_id);
        return "redirect:/admin/archive/1";
    }

}
