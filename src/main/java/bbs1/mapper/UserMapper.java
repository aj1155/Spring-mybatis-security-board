package bbs1.mapper;

import java.util.List;

import bbs1.dto.Pagination;
import bbs1.dto.User;

public interface UserMapper {
    User selectById(int id);
    User selectByLoginId(String loginId);
    List<User> selectAll();
    List<User> selectPage(Pagination pagination);
    int selectCount(Pagination pagination);
    void insert(User user);
    void update(User user);
    void delete(int id);
}
