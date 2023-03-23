package com.itheima.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.entity.OrderDetail;
import com.itheima.mapper.OrderDetailMapper;
import com.itheima.service.OrderDetailServicce;
import com.itheima.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderDetailServicceImpl
 * @Description TODO
 * @Author Bai
 * @Date 2023/3/19 14 : 08
 */
@Service
public class OrderDetailServicceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailServicce {

}
