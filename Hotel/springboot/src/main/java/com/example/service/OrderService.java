package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.OrderEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Order;
import com.example.entity.Type;
import com.example.exception.CustomException;
import com.example.mapper.OrderMapper;
import com.example.mapper.TypeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.relation.Role;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private TypeMapper typeMapper;
    private Long getDayNum(String inTime,String outTime) throws ParseException{
        DateFormat dft=new SimpleDateFormat("yyyy-MM-dd");
        Date star=dft.parse(inTime);//开始时间
        Date endDay=dft.parse(outTime);//结束时间
        int result = star.compareTo(endDay);
        if (result >= 0) {
            throw new CustomException(ResultCodeEnum.TIME_CHECK_ERROR);
        }
        Long starTime=star.getTime();
        Long endTime=endDay.getTime();
        long num=endTime-starTime;//时间戳相差的毫秒数
        return num/24/60/60/1000;
    }
    public void add(Order order){
        try{
            Long dayNum = getDayNum(order.getInTime(), order.getOutTime());
            order.setDays(dayNum);
            order.setStatus(OrderEnum.STATUS_CHECKING.status);
            order.setTime(DateUtil.now());
            order.setOrderId(DateUtil.format(new Date(), "yyyyMMddHHmmss"));
            Type type = typeMapper.selectById(order.getTypeId());
            order.setPrice(type.getPrice() * dayNum);
            orderMapper.insert(order);
        }catch(CustomException e){
            throw new CustomException(ResultCodeEnum.TIME_CHECK_ERROR);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void deleteById(Integer id){
        orderMapper.deleteById(id);
    }
    public void deleteBatch(List<Integer> ids){
        for (Integer id:ids){
            orderMapper.deleteById(id);
        }
    }
    public void updateById(Order order){
        orderMapper.updateById(order);
    }
    public Order selectById(Integer id){
        return orderMapper.selectById(id);
    }
    public List<Order> selectAll(Order order){
        return orderMapper.selectAll(order);
    }
    public PageInfo<Order> selectPage(Order order,Integer pageNum,Integer pageSize){
        Account currentUser= TokenUtils.getCurrentUser();
        if (RoleEnum.HOTEL.name().equals(currentUser.getRole())){
            order.setHotelId(currentUser.getId());
        }
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            order.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Order> list=orderMapper.selectAll(order);
        return PageInfo.of(list);
    }
    public List<Order> selectByUserId(Integer id){
        Order order=new Order();
        order.setUserId(id);
        return orderMapper.selectAll(order);
    }

    public List<Order> selectByCheckingAndHotelId(Integer id) {
        Order order = new Order();
        order.setHotelId(id);
        order.setStatus(OrderEnum.STATUS_CHECKING.status);
        return orderMapper.selectAll(order);
    }
}
