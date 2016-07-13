package bbs1.mapper;

import java.util.List;

import bbs1.dto.Article;
import bbs1.dto.Pagination;

public interface ArticleMapper {
    Article selectById(int id);
    List<Article> selectPage(Pagination pagination);
    int selectCount(Pagination pagination);
}
