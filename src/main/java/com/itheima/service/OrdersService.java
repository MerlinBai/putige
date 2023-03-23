package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.entity.Orders;
import com.itheima.mapper.OrdersMapper;

public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);

}
