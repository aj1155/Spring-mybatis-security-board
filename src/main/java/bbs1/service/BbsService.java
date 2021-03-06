package bbs1.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bbs1.dto.Article;
import bbs1.dto.User;
import bbs1.mapper.ArticleImageMapper;


@Service
public class BbsService {
	
	@Autowired ArticleImageMapper articleImageMapper;
	public boolean isAuthor(Article article) {
        return (UserService.getCurrentUser() != null) &&
               (article.getUserId() == UserService.getCurrentUser().getId());
    }

    public boolean canCreate(int boardId) {
        User user = UserService.getCurrentUser();
        if (user == null) return false;
        if ("관리자".equals(user.getUserType())) return true;
        if ("교수".equals(user.getUserType())) return true;
        return boardId == 2;
    }

    public boolean canEdit(Article article) {
        return isAuthor(article);
    }

    public boolean canDelete(Article article) {
        User user = UserService.getCurrentUser();
        if (user == null) return false;
        if ("관리자".equals(user.getUserType())) return true;
        return isAuthor(article);
    }

    public String validate(Article article) {
        if (StringUtils.isBlank(article.getTitle()))
            return "제목을 입력하세요";
        if (StringUtils.isBlank(article.getBody()))
            return "내용을 입력하세요";
        return null;
    }
    
    public void updateArticleImage(Article article) {
        articleImageMapper.deleteByArticleId(article.getId());
        String pattern = "bbs/([0-9]+)/image.do";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(article.getBody());
        while (m.find()) {
            int imageId = Integer.parseInt(m.group(1));
            articleImageMapper.insert(article.getId(), imageId);
        }
    }



}
