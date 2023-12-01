package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.StatusEnum;
import com.example.entity.Account;
import com.example.entity.Hotel;
import com.example.exception.CustomException;
import com.example.mapper.HotelMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HotelService {
    @Resource
    private HotelMapper hotelMapper;
    public void add(Hotel hotel){
        Hotel hotelUser=hotelMapper.selectByUsername(hotel.getUsername());
        if (ObjectUtil.isNotEmpty(hotelUser)){
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(hotel.getPassword())){
            hotel.setPassword("123456");
        }
        if (ObjectUtil.isEmpty(hotel.getRole())) {
            hotel.setRole(RoleEnum.HOTEL.name());
        }
        if (ObjectUtil.isEmpty(hotel.getAvatar())) {
            hotel.setAvatar("http://localhost:9090/files/1697438073596-avatar.png");
        }
        hotel.setStatus(StatusEnum.CHECKING.status);
        hotelMapper.insert(hotel);
    }
    public PageInfo<Hotel> selectPage(Hotel hotel, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Hotel> hotels = hotelMapper.selectAll(hotel);
        return PageInfo.of(hotels);
    }

    public void update(Hotel hotel) {
        hotelMapper.updateById(hotel);
    }

    public void deleteById(Integer id) {
        hotelMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> list) {
        for (Integer id : list) {
            deleteById(id);
        }
    }

    public Account login(Account account) {
        Account hotel=hotelMapper.selectByUsername(account.getUsername());
        if(ObjectUtil.isNull(hotel)){
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if(!account.getPassword().equals(hotel.getPassword())){
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        String tokenData=hotel.getId()+"-"+RoleEnum.HOTEL.name();
        String token= TokenUtils.createToken(tokenData,hotel.getPassword());
        hotel.setToken(token);
        return hotel;
    }

    public Hotel selectById(Integer id) {
        return hotelMapper.selectById(id);
    }

    public void register(Account account) {
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(account, hotel);
        add(hotel);
    }
    public void updatePassword(Account account) {
        Hotel dbHotel = hotelMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbHotel)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbHotel.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbHotel.setPassword(account.getNewPassword());
        hotelMapper.updateById(dbHotel);
    }

    public List<Hotel> selectAll() {
        return hotelMapper.selectAll(new Hotel());
    }
    public List<Hotel> selectByName(String name) {
        Hotel hotel = new Hotel();
        if (ObjectUtil.isNotEmpty(name) && !"null".equals(name)) {
            hotel.setName(name);
        }
        return hotelMapper.selectAll(hotel);
    }
}
