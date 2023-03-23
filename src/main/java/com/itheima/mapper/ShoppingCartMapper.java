package com.itheima.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName ShoppingCartMapper
 * @Description TODO
 * @Author Bai
 * @Date 2023/3/19 11 : 01
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
