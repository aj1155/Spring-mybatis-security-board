package bbs1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import bbs1.dto.Article;
import bbs1.dto.Pagination;
import bbs1.mapper.ArticleMapper;
import bbs1.service.UserService;

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
        Article article = articleMapper.selectById(id);
        boolean isAuthor = (UserService.getCurrentUser() != null) &&
                           (article.getUserId() == UserService.getCurrentUser().getId());
        if (!isAuthor) articleMapper.updateReadCount(id);
        model.addAttribute("article", article);
        return "bbs/article";
    }

    @Secured("ROLE_전체")
    @RequestMapping(value="/bbs/create.do", method=RequestMethod.GET)
    public String create(Model model, Pagination pagination) {
        return "bbs/create";
    }

    @Secured("ROLE_전체")
    @RequestMapping(value="/bbs/create.do", method=RequestMethod.POST)
    public String create(Model model, Pagination pagination, Article article) {
        article.setBoardId(pagination.getBoardId());
        article.setUserId(UserService.getCurrentUser().getId());
        articleMapper.insert(article);
        return "redirect:/bbs/list.do?bd=" + pagination.getBd();
    }

}
