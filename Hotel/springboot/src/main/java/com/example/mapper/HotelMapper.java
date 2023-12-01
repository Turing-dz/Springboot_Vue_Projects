package com.example.mapper;

import com.example.entity.Account;
import com.example.entity.Hotel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HotelMapper {
    void insert(Hotel hotel);//增
    @Delete("delete from hotel where id=#{id}")
    void deleteById(Integer id);//删
    void updateById(Hotel hotel);//改
    @Select("select * from hotel where username=#{username}")
    Hotel selectByUsername(String username);//查
    List<Hotel> selectAll(Hotel hotel);//查所有
    @Select("select * from hotel where id = #{id}")
    Hotel selectById(Integer id);
}
