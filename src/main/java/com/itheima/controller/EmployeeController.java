package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.entity.Employee;
import com.itheima.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @ClassName EmployeeController
 * @Description TODO
 * @Author Bai
 * @Date 2023/3/13 20 : 34
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
    //1.将页面提交的密码进行md5加密
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.判断有没有查到信息
        if(emp==null){
            return R.error("登录失败");
        }
        //密码比对
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }
        //状态比对
        if (emp.getStatus()==0){
            return R.error("登录失败");
        }
        //登录成功
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
    return R.success("退出成功");
    }


    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
    employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
    //employee.setCreateTime(LocalDateTime.now());
    //employee.setUpdateTime(LocalDateTime.now());
    //  获取用户id
       // long empId = (long) request.getSession().getAttribute("employee");
       // employee.setCreateUser(empId);
       // employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }



    @GetMapping("/page")
    public R<Page>page(int page,int pageSize,String name){

    //构造分页构造
      Page pageInfo=new Page();
      //构造条件构造器
        LambdaQueryWrapper<Employee> QueryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        QueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        QueryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,QueryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> updata(HttpServletRequest request,@RequestBody Employee employee){
        //long empId = (long) request.getSession().getAttribute("employee");
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable long id){
        log.info("根据id查询员工信息");
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");

    }
}
