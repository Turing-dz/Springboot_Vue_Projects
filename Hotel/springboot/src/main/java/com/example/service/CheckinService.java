package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.OrderEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.RoomEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.CheckinMapper;
import com.example.mapper.OrderMapper;
import com.example.mapper.RoomMapper;
import com.example.mapper.TypeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CheckinService {
    @Resource
    private CheckinMapper checkinMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private TypeMapper typeMapper;

    public void add(Checkin checkin){
       String orderId= checkin.getOrderId();
       Order order=orderMapper.selectByOrderId(orderId);
       checkin.setUserId(order.getUserId());
       checkin.setHotelId(order.getHotelId());
       checkin.setTypeId(order.getTypeId());
       checkinMapper.insert(checkin);
        // 1. 对应的客房状态变成 占用
        Room room=roomMapper.selectById(checkin.getRoomId());
        room.setStatus(RoomEnum.STATUS_NO.status);
        roomMapper.updateById(room);
        // 2. 对应的客房分类剩余间数 要减 1
        Type type=typeMapper.selectById(checkin.getTypeId());
        type.setNum(type.getNum()-1);
        typeMapper.updateById(type);
        // 3. 对应的订单状态 要变成 已入住
        order.setStatus(OrderEnum.STATUS_IN.status);
        orderMapper.updateById(order);
    }
    public void deleteById(Integer id){
        checkinMapper.deleteById(id);
    }
    public void deleteBatch(List<Integer> ids){
        for (Integer id:ids){
            checkinMapper.deleteById(id);
        }
    }
    public void updateById(Checkin checkin){
        checkinMapper.updateById(checkin);
    }
    public Checkin selectById(Integer id){
        return checkinMapper.selectById(id);
    }
    public List<Checkin> selectAll(Checkin checkin){
        return checkinMapper.selectAll(checkin);
    }
    public PageInfo<Checkin> selectPage(Checkin checkin,Integer pageNum,Integer pageSize){
        Account currentUser= TokenUtils.getCurrentUser();
        if (RoleEnum.HOTEL.name().equals(currentUser.getRole())){
            checkin.setHotelId(currentUser.getId());
        }
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            checkin.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Checkin> list=checkinMapper.selectAll(checkin);
        return PageInfo.of(list);
    }
    public List<Checkin> selectByUserId(Integer id){
        Checkin checkin = new Checkin();
        checkin.setUserId(id);
        return checkinMapper.selectAll(checkin);
    }
    public void checkout(Integer id){
        Checkin checkin=checkinMapper.selectById(id);
        checkin.setOutTime(DateUtil.now());
        checkinMapper.updateById(checkin);
        // 1. 该客房状态需要还原为 空闲
        Room room = roomMapper.selectById(checkin.getRoomId());
        room.setStatus(RoomEnum.STATUS_OK.status);
        roomMapper.updateById(room);

        // 2. 该客房对应的房型剩余房间数需要还原（+1）
        Type type = typeMapper.selectById(checkin.getTypeId());
        type.setNum(type.getNum() + 1);
        typeMapper.updateById(type);

        // 3. 对应的订单状态 要变成 已退房
        Order order = orderMapper.selectByOrderId(checkin.getOrderId());
        order.setStatus(OrderEnum.STATUS_OUT.status);
        orderMapper.updateById(order);
    }
}
