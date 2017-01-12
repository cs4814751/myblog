package com.leibro.controller;

import com.leibro.model.Blog;
import com.leibro.service.VisitService;
import com.leibro.utils.MarkdownToHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by leibro on 2017/1/6.
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {
    @Autowired
    VisitService visitService;

    @RequestMapping("/{blog_uri}")
    public String viewBlog(@PathVariable("blog_uri") String blog_uri, Model model) {
        visitService.visitBlog(blog_uri,model);
        return "blog-view";
    }

    @RequestMapping("/archive/{year}")
    public String archive(Model model,@PathVariable("year") int year) {
        visitService.blogYearArchive(year,model);
        return "blogs-archive";
    }

    @RequestMapping(value = "/search",params = {"keyword"})
    public String searchBlogsByKeyword(@RequestParam("keyword") String keyword,Model model) {
        visitService.searchBlogsByKeyword(keyword,model);
        return "search";
    }

    @RequestMapping(value = "/search",params = {"tag"})
    public String searchBlogsByTag(@RequestParam("tag") String tag,Model model) {
        visitService.searchBlogsByTag(tag,model);
        return "search";
    }
}
