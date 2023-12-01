package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Type;
import com.example.exception.CustomException;
import com.example.mapper.TypeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TypeService {
    @Resource
    private TypeMapper typeMapper;

    /**
     * 新增
     */
    public void add(Type type) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        type.setHotelId(currentUser.getId());
        typeMapper.insert(type);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        typeMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            typeMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Type type) {
        typeMapper.updateById(type);
    }

    /**
     * 根据ID查询
     */
    public Type selectById(Integer id) {
        return typeMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Type> selectAll(Type type) {
        return typeMapper.selectAll(type);
    }

    /**
     * 分页查询
     */
    public PageInfo<Type> selectPage(Type type, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        if (RoleEnum.HOTEL.name().equals(currentUser.getRole())) {
            type.setHotelId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Type> list = typeMapper.selectAll(type);
        return PageInfo.of(list);
    }

    public List<Type> selectByHotelId(Integer hotelId) {
        return typeMapper.selectByHotelId(hotelId);
    }
}
