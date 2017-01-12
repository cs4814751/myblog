package com.leibro.model.wrapper;

import com.leibro.model.Blog;
import com.leibro.model.Tag;

import java.util.List;

/**
 * Created by leibro on 2017/1/6.
 */
public class BlogAndTagsWrapper {
    private Blog blog;
    private List<Tag> tags;

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
