package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.RoomEnum;
import com.example.common.enums.StatusEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.HotelMapper;
import com.example.mapper.OrderMapper;
import com.example.mapper.RoomMapper;
import com.example.mapper.TypeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoomService {
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private TypeMapper typeMapper;
    @Resource
    private OrderMapper orderMapper;
    /**
     * 新增
     */
    public void add(Room room) {
        room.setStatus(RoomEnum.STATUS_OK.status);
        roomMapper.insert(room);

        // 向对应的酒店里面该分类下面的房间数量+1
        Type type = typeMapper.selectById(room.getTypeId());
        if (ObjectUtil.isNotEmpty(type)) {
            type.setNum(type.getNum() + 1);
            typeMapper.updateById(type);
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        roomMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            roomMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Room room) {
        roomMapper.updateById(room);
    }

    /**
     * 根据ID查询
     */
    public Room selectById(Integer id) {
        return roomMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Room> selectAll(Room room) {
        return roomMapper.selectAll(room);
    }

    /**
     * 分页查询
     */
    public PageInfo<Room> selectPage(Room room, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.HOTEL.name().equals(currentUser.getRole())) {
            room.setHotelId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Room> list = roomMapper.selectAll(room);
        return PageInfo.of(list);
    }

    public List<Room> selectByTypeId(String orderId) {
        Order order = orderMapper.selectByOrderId(orderId);
        return roomMapper.selectByTypeId(order.getTypeId());
    }
}
