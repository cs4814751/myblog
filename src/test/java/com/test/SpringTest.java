package com.test;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;


import com.leibro.dao.BlogDao;
import com.leibro.dao.BlogMapper;
import com.leibro.model.Blog;
import com.leibro.service.VisitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leibro on 2017/1/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:spring-config/applicationContext.xml")
public class SpringTest {
    @Autowired
    BlogMapper blogMapper;

    @Autowired
    VisitService visitService;

    @Autowired
    RedisTemplate template;

    @Autowired
    BlogDao blogDao;

    @Test
    public void test() throws PinyinException {
        String str = "这是一个文章的标题";
        String result = PinyinHelper.convertToPinyinString(str,"-", PinyinFormat.WITHOUT_TONE);
        System.out.println(result);
        Blog blog = new Blog();
        blog.setAuthor(1);
        blog.setContent("这是博客的内容");
        blog.setCreateTime(new Date());
        blog.setLastModify(new Date());
        blog.setTitle("这是文章的标题");
        blog.setReadCount(1);
        blog.setUri(PinyinHelper.convertToPinyinString(blog.getTitle(),"-",PinyinFormat.WITHOUT_TONE));
        int returnResult = blogMapper.insertSelective(blog);
        System.out.println(returnResult);
        System.out.println(blog.getId());
    }

    @Test
    public void test2() {
        String str = "<div id=\"content\" style=\"margin-bottom: 20px\" class=\"markdown-body\"><h1 id=\"-\">这是文章的一级标题</h1>\n" +
                "<h2 id=\"-\">这是文章的二级标题</h2>\n" +
                "<p>豫章故郡，洪都新府。星分翼轸，地接衡庐。襟三江而带五湖，控蛮荆而引瓯越。物华天宝，龙光射牛斗之墟；人杰地灵，徐孺下陈蕃之榻。雄州雾列，俊采星驰。台隍枕夷夏之交，宾主尽东南之美。都督阎公之雅望，棨戟遥临；宇文新州之懿范，襜帷暂驻。十旬休假，胜友如云；千里逢迎，高朋满座。腾蛟起凤，孟学士之词宗；紫电青霜，王将军之武库。家君作宰，路出名区；童子何知，躬逢胜饯。<br>时维九月，序属三秋。潦水尽而寒潭清，烟光凝而暮山紫。俨骖騑于上路，访风景于崇阿；临帝子之长洲，得天人之旧馆。层峦耸翠，上出重霄；飞阁流丹，下临无地。鹤汀凫渚，穷岛屿之萦回；桂殿兰宫，即冈峦之体势。</p>\n" +
                "<pre><code>public static void main(String[] args) {\n" +
                "    System.out.pritnln(\"Hello World\");\n" +
                "}\n" +
                "</code></pre><ol>\n" +
                "<li>这是1</li>\n" +
                "<li>这是2</li>\n" +
                "<li>这是3</li>\n" +
                "</ol>\n" +
                "<p><img src=\"http://oizochynr.bkt.clouddn.com/2017-01-07-2017-01-07%2010_25_47.gif\" alt=\"\"></p>\n" +
                "</div>";
        System.out.print(Pattern.compile("</?[^>]+>").matcher(str).replaceAll(""));
    }


    @Test
    public void test4() {
        Blog blog = blogMapper.selectByUri("zhe-shi-yi-pian-ce-shi-wen-zhang-a-a");
        Matcher matcher = Pattern.compile("!\\[[^\\]]*]\\([^\\)]*\\.(png|jpeg|jpg|gif|bmp)\\)").matcher(blog.getContent());
        String firstImgMatchResult = null;
        while (matcher.find()) {
            firstImgMatchResult = matcher.group(0);
        }
        System.out.println(firstImgMatchResult);
        Matcher matcher1 = Pattern.compile("(?<=[(]).*(?=[)])").matcher(firstImgMatchResult);
        matcher1.find();
        String imgUrl = matcher1.group(0);
        System.out.println(imgUrl);
    }

    @Test
    public void test5() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.matches("Zhulei4814751","$2a$10$7csX4X1dZzRznQiSW8iyAuvg92scHziabKoEqSRktyWNI8oj5VRNi"));
    }

    @Test
    public void test6() {
        blogMapper.selectByPrimaryKey(19);
    }

    @Test
    public void test7() {
        Blog blog = new Blog();
        blog.setId(19);
        for(int i = 0;i < 5;i ++) {
            blogDao.addViewCount(19);
        }

    }

    @Test
    public void test8() {
        ZSetOperations operations = template.opsForZSet();
        Set<Integer> entries = operations.range("count",0,-1);
        if(entries.size() > 0) {
            for (int id : entries) {
                double score = operations.score("count", id);
                System.out.println(score);
            }
        }
    }

    @Test
    public void test9() {
        Blog blog = blogDao.selectByPrimaryKey(19);
        System.out.print(blog.getTitle());
    }


}
