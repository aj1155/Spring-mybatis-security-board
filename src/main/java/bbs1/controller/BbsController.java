package bbs1.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import bbs1.dto.Article;
import bbs1.dto.File;
import bbs1.dto.Image;
import bbs1.dto.Pagination;
import bbs1.mapper.ArticleImageMapper;
import bbs1.mapper.ArticleMapper;
import bbs1.mapper.FileMapper;
import bbs1.mapper.ImageMapper;
import bbs1.service.BbsService;
import bbs1.service.UserService;

@Controller
public class BbsController {
    @Autowired ArticleMapper articleMapper;
    @Autowired FileMapper fileMapper;
    @Autowired ImageMapper imageMapper;
    @Autowired ArticleImageMapper articleImageMapper;
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
            bbsService.updateArticleImage(article);
            imageMapper.deleteOrphan();
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
            return "bbs/create";
        }
        if (bbsService.canEdit(articleMapper.selectById(article.getId()))) {
            articleMapper.update(article);
            bbsService.updateArticleImage(article);
            imageMapper.deleteOrphan();
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
            articleImageMapper.deleteByArticleId(id);
            articleMapper.delete(id);
            imageMapper.deleteOrphan();
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

    @RequestMapping("/bbs/{id}/image.do")
    public void image(@PathVariable("id") int id, HttpServletResponse response) throws IOException {
        Image image = imageMapper.selectById(id);
        if (image == null) return;
        String fileName = URLEncoder.encode(image.getFileName(),"UTF-8");
        response.setContentType(image.getMimeType());
        response.setHeader("Content-Disposition", "filename=" + fileName + ";");
        try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
            output.write(image.getData());
        }
    }

    @Secured("ROLE_전체")
    @RequestMapping(value="/bbs/smartEditorUpload.do", method=RequestMethod.POST)
    public String photoUpload(@RequestParam("callback") String callback,
            @RequestParam("callback_func") String callback_func,
            @RequestParam("Filedata") MultipartFile uploadedFile) throws IOException {
        String r;
        String fname = Paths.get(uploadedFile.getOriginalFilename()).getFileName().toString();
        if (uploadedFile.getSize() > 0 ) {
            Image image = new Image();
            image.setUserId(UserService.getCurrentUser().getId());
            image.setFileName(fname);
            image.setFileSize((int)uploadedFile.getSize());
            image.setData(uploadedFile.getBytes());
            imageMapper.insert(image);
            r = "&bNewLine=true&sFileName=" + fname +
                "&sFileURL=/bbs1/bbs/"+image.getId()+"/image.do";
        } else
            r = "&errstr=" + fname;
        String url = callback + "?callback_func=" + callback_func + r;
        return "redirect:" + url;
    }

    @Secured("ROLE_전체")
    @RequestMapping(value="/bbs/smartEditorUpload5.do", method=RequestMethod.POST)
    public void multiplePhotoUpload(HttpServletRequest request, HttpServletResponse response){
            try {
                 String fileName = request.getHeader("file-name");
                 int fileSize = Integer.parseInt(request.getHeader("file-size"));
                 InputStream input = request.getInputStream();
                 int count = 0;
                 byte[] data = new byte[fileSize];
                 while (count < fileSize)
                     count += input.read(data, count, data.length - count);

                 Image image = new Image();
                 image.setUserId(UserService.getCurrentUser().getId());
                 image.setFileName(fileName);
                 image.setFileSize(fileSize);
                 image.setData(data);
                 imageMapper.insert(image);

                 String s = "&bNewLine=true&sFileName="+ fileName;
                 s += "&sFileURL=/bbs1/bbs/"+image.getId()+"/image.do";
                 PrintWriter out = response.getWriter();
                 out.print(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}