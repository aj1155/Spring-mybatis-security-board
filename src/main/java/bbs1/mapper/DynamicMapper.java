package bbs1.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import bbs1.dto.User;


public interface DynamicMapper {
	List<User> selectTop(@Param("count") int count,@Param("order") String order);
	List<User> selectByNameOrLoginId1(User user);
	List<User> selectByNameOrLoginId2(User user);
	List<User> selectByIdList(@Param("idList") int[] idList);
	void update(User user);
}
