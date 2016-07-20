package bbs1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import bbs1.dto.Department;
import bbs1.dto.User;
import bbs1.mapper.DepartmentMapper;
import bbs1.mapper.DynamicMapper;
import bbs1.mapper.UserMapper;

@Controller
public class MybatisController {


    @Autowired DepartmentMapper departmentMapper;
    @Autowired UserMapper userMapper;
    @Autowired DynamicMapper dynamicMapper;

    @RequestMapping(value="/mybatis/cacheTest.do", method=RequestMethod.GET)
    public String cacheTest(Model model) {
        List<Department> departments = departmentMapper.selectAll();
        model.addAttribute("departments", departments);
        model.addAttribute("department", departments.get(0));
        return "mybatis/cacheTest";
    }

    @RequestMapping(value="/mybatis/cacheTest.do", method=RequestMethod.POST)
    public String cache(Model model, Department department) {
        departmentMapper.update(department);
        model.addAttribute("departments", departmentMapper.selectAll());
        return "mybatis/cacheTest";
    }

    @RequestMapping("/mybatis/departmentUserList1.do")
    public String departmentUserList1(Model model) {
        List<Department> departments = departmentMapper.selectAll();
        for (Department department : departments) {
            List<User> users = userMapper.selectByDepartmentId(department.getId());
            department.setUsers(users);
        }
        model.addAttribute("departments", departments);
        return "mybatis/departmentUserList";
    }

    @RequestMapping("/mybatis/departmentUserList2.do")
    public String departmentUserList2(Model model) {
        model.addAttribute("departments", departmentMapper.selectAllWithUsers());
        return "mybatis/departmentUserList";
    }

    @RequestMapping("/mybatis/dynamicSQL.do")
    public String dynamicSQL(Model model) {
        User user1 = new User(); user1.setId(2); user1.setEmail("lsj@gmail.com");
        User user2 = new User(); user2.setName("이승진");
        User user3 = new User(); user3.setLoginId("lsj");
        dynamicMapper.update(user1);
        model.addAttribute("list1", dynamicMapper.selectTop(3, "name DESC"));
        model.addAttribute("list2", dynamicMapper.selectByNameOrLoginId1(user2));
        model.addAttribute("list3", dynamicMapper.selectByNameOrLoginId2(user3));
        model.addAttribute("list4", dynamicMapper.selectByIdList(new int[] { 1, 2, 3 }));
        return "mybatis/dynamicSQL";
    }
}
