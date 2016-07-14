package bbs1.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import bbs1.dto.File;

public interface FileMapper {
	List<File> selectByArticleId(int articleId);
    File selectById(int id);
    void insert(File file);
    void delete(int id);
    void deleteByArticleId(int articleId);
}
