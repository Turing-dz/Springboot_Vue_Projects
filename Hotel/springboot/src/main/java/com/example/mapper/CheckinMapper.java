package com.example.mapper;

import com.example.entity.Checkin;
import com.example.entity.Order;

import java.util.List;

public interface CheckinMapper {
    int insert(Checkin checkin);
    int deleteById(Integer id);
    int updateById(Checkin checkin);
    Checkin selectById(Integer id);
    List<Checkin> selectAll(Checkin checkin);
}
