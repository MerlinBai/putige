package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.dto.SetmealDto;
import com.itheima.entity.Category;
import com.itheima.entity.Setmeal;
import com.itheima.service.CategoryService;
import com.itheima.service.SetmealDishService;
import com.itheima.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName SetmealController
 * @Description TODO
 * @Author Bai
 * @Date 2023/3/16 19 : 02
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

    setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
        }



    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器
       Page<Setmeal> pageInfo = new Page<>(page, pageSize);
       Page<SetmealDto> dtoPage = new Page<>();

       LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
       //添加查询条件
            queryWrapper.like(name!=null,Setmeal::getName,name);
            //添加排序条件
            queryWrapper.orderByDesc(Setmeal::getUpdateTime);

            setmealService.page(pageInfo,queryWrapper);

            //对象拷贝
            BeanUtils.copyProperties(pageInfo,dtoPage,"records");

            List<Setmeal> records = pageInfo.getRecords();

            List<SetmealDto> list = records.stream().map((item) ->{

                SetmealDto setmealDto = new SetmealDto();
                //对象拷贝
                BeanUtils.copyProperties(item,setmealDto);
                //分类id
                Long categoryId = item.getCategoryId();
                //根据分类id获取分类对象
                Category category = categoryService.getById(categoryId);
                if (category!=null){
                //分类名称
                    String categoryName = category.getName();
                    setmealDto.setCategoryName(categoryName);
                }
                return setmealDto;
                    }).collect(Collectors.toList());

    dtoPage.setRecords(list);
    return R.success(dtoPage);
        }


    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
        public R<String> delete(@RequestParam List<Long> ids){
         setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功");

        }


        @GetMapping("/list")
        public R<List<Setmeal>> list(Setmeal setmeal){
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
            queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
            queryWrapper.orderByDesc(Setmeal::getUpdateTime);
            List<Setmeal> list = setmealService.list(queryWrapper);

            return R.success(list);
        }
}
