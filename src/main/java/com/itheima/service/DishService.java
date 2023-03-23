package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.DishDto;
import com.itheima.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应口味的数据，需要操作两张表： dish、 dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);
    //更新菜品信息
    void updateWithFlavor(DishDto dishDto);
}
