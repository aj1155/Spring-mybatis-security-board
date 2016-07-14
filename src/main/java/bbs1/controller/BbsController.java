package bbs1.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import bbs1.dto.Article;
import bbs1.dto.File;
import bbs1.dto.Pagination;
import bbs1.mapper.ArticleMapper;
import bbs1.mapper.FileMapper;
import bbs1.service.BbsService;
import bbs1.service.UserService;

@Controller
public class BbsController {
    @Autowired ArticleMapper articleMapper;
    @Autowired FileMapper fileMapper;
    @Autowired BbsService bbsService;

    @RequestMapping("/bbs/list.do")
    public String list(Model model, Pagination pagination) {
        pagination.setRecordCount(articleMapper.selectCount(pagination));
        model.addAttribute("list", articleMapper.selectPage(pagination));
        return "bbs/list";
    }

    @RequestMapping("/bbs/article.do")
    public String article(Model model, @RequestParam("id") int id, Pagination pagination) {
        Article article = articleMapper.selectById(id);
        if (!bbsService.isAuthor(article)) articleMapper.updateReadCount(id);
        model.addAttribute("article", article);
        model.addAttribute("canEdit", bbsService.canEdit(article));
        model.addAttribute("canDelete", bbsService.canDelete(article));
        model.addAttribute("files", fileMapper.selectByArticleId(id));
        return "bbs/article";
    }

    @Secured("ROLE_전체")
    @RequestMapping(value="/bbs/create.do", method=RequestMethod.GET)
    public String create(Model model, Pagination pagination) {
        return "bbs/create";
    }

    @Secured("ROLE_전체")
    @RequestMapping(value="/bbs/create.do", method=RequestMethod.POST)
    public String create(Model model, Pagination pagination, Article article,
            @RequestParam("file") MultipartFile[] uploadedFiles) throws IOException {
        String errMsg = bbsService.validate(article);
        if (errMsg != null) {
            model.addAttribute("errMsg", errMsg);
            return "bbs/create";
        }
        if (bbsService.canCreate(pagination.getBoardId())) {
            article.setBoardId(pagination.getBoardId());
            article.setUserId(UserService.getCurrentUser().getId());
            articleMapper.insert(article);
            for (MultipartFile uploadedFile : uploadedFiles) {
                File file = new File();
                if (uploadedFile.getSize() > 0 ) {
                    file.setArticleId(article.getId());
                    file.setUserId(UserService.getCurrentUser().getId());
                    file.setFileName(Paths.get(uploadedFile.getOriginalFilename()).getFileName().toString());
                    file.setFileSize((int)uploadedFile.getSize());
                    file.setData(uploadedFile.getBytes());
                    fileMapper.insert(file);
                }
            }

        }
        return "redirect:/bbs/list.do?bd=" + pagination.getBd();
    }

    @Secured("ROLE_전체")
    @RequestMapping(value="/bbs/edit.do", method=RequestMethod.GET)
    public String edit(Model model, @RequestParam("id") int id, Pagination pagination) {
        model.addAttribute("article", articleMapper.selectById(id));
        model.addAttribute("files", fileMapper.selectByArticleId(id));
        return "bbs/edit";
    }

    @Secured("ROLE_전체")
    @RequestMapping(value="/bbs/edit.do", method=RequestMethod.POST, params="cmd=save")
    public String edit(Model model, Pagination pagination, Article article,
            @RequestParam("file") MultipartFile[] uploadedFiles) throws IOException {
        String errMsg = bbsService.validate(article);
        if (errMsg != null) {
            model.addAttribute("errMsg", errMsg);
            return "bbs/edit";
        }
        if (bbsService.canEdit(articleMapper.selectById(article.getId()))) {
            articleMapper.update(article);
            for (MultipartFile uploadedFile : uploadedFiles) {
                File file = new File();
                if (uploadedFile.getSize() > 0 ) {
                    file.setArticleId(article.getId());
                    file.setUserId(UserService.getCurrentUser().getId());
                    file.setFileName(Paths.get(uploadedFile.getOriginalFilename()).getFileName().toString());
                    file.setFileSize((int)uploadedFile.getSize());
                    file.setData(uploadedFile.getBytes());
                    fileMapper.insert(file);
                }
            }

        }
        return "redirect:/bbs/article.do?id="+article.getId()+"&"+ pagination.getQueryString();
    }

    @Secured("ROLE_전체")
    @RequestMapping(value="/bbs/edit.do", method=RequestMethod.POST, params="cmd=deleteFile")
    public String edit(Model model, Pagination pagination, Article article, 
            @RequestParam("fileId") int fileId) {
        if (bbsService.canEdit(articleMapper.selectById(article.getId())))
            fileMapper.delete(fileId);
        model.addAttribute("files", fileMapper.selectByArticleId(article.getId()));
        return "bbs/edit";
    }


    @Secured("ROLE_전체")
    @RequestMapping("/bbs/delete.do")
    public String delete(Model model, @RequestParam("id") int id, Pagination pagination) {
    	if (bbsService.canDelete(articleMapper.selectById(id))) {
            fileMapper.deleteByArticleId(id);
            articleMapper.delete(id);
        }
        return "redirect:/bbs/list.do?" + pagination.getQueryString();
    }

    @RequestMapping("/bbs/download.do")
    public void download(@RequestParam("id") int id, HttpServletResponse response) throws IOException {
        File file = fileMapper.selectById(id);
       if (file == null) return;
        String fileName = URLEncoder.encode(file.getFileName(),"UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";");
        try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
            output.write(file.getData());
        }
    }

}
