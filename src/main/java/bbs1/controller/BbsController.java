package bbs1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import bbs1.dto.Pagination;
import bbs1.mapper.ArticleMapper;

@Controller
public class BbsController {
    @Autowired ArticleMapper articleMapper;

    @RequestMapping("/bbs/list.do")
    public String list(Model model, Pagination pagination) {
        pagination.setRecordCount(articleMapper.selectCount(pagination));
        model.addAttribute("list", articleMapper.selectPage(pagination));
        return "bbs/list";
    }

    @RequestMapping("/bbs/article.do")
    public String article(Model model, @RequestParam("id") int id, Pagination pagination) {
        model.addAttribute("article", articleMapper.selectById(id));
        return "bbs/article";
    }

}

