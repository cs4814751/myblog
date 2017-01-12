package com.leibro.controller;

import com.leibro.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by leibro on 2017/1/1.
 */
@Controller
public class HomeController {
    @Autowired
    VisitService visitService;

    @RequestMapping("/main")
    public String test() {
        return "hello";
    }

    @RequestMapping("/test")
    public String test2() {
        return "test";
    }

    @RequestMapping("/home")
    public String visitHome(Model model) {
        visitService.getBlogsForHomeByOffset(model,0);
        return "home";
    }

    @RequestMapping(value = "/home",params = {"offset"})
    public String visitHomeWithOffset(Model model, @RequestParam("offset") int offset) {
        visitService.getBlogsForHomeByOffset(model,offset);
        return "home :: #card-wrapper";
    }
}
