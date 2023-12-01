package com.example.mapper;

import com.example.entity.Order;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderMapper {
    int insert(Order order);
    int deleteById(Integer id);
    int updateById(Order order);
    Order selectById(Integer id);
    List<Order> selectAll(Order order);
    @Select("select * from orders where order_id = #{orderId}")
    Order selectByOrderId(String orderId);
}
