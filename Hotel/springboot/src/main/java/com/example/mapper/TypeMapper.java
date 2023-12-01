package com.example.mapper;

import com.example.entity.Account;
import com.example.entity.Hotel;
import com.example.entity.Type;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TypeMapper {
    void insert(Type type);//增
    void deleteById(Integer id);//删
    void updateById(Type type);//改

    List<Type> selectAll(Type type);//查所有
    Type selectById(Integer id);//查


    @Select("select * from type where hotel_id = #{hotelId}")
    List<Type> selectByHotelId(Integer hotelId);
}
