package bbs1.mapper;

import java.util.List;

import bbs1.dto.Department;

public interface DepartmentMapper {
	List<Department> selectAll();
    List<Department> selectAllWithUsers();
    void update(Department department);
}
