package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.StatusEnum;
import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.entity.Hotel;
import com.example.entity.Type;
import com.example.exception.CustomException;
import com.example.mapper.CollectMapper;
import com.example.mapper.HotelMapper;
import com.example.mapper.TypeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CollectService {
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private TypeMapper typeMapper;

    /**
     * 新增
     */
    public void add(Collect collect) {
        Collect dbCollect = collectMapper.selectByUserIdAndType(collect.getUserId(), collect.getTypeId());
        if (ObjectUtil.isNotEmpty(dbCollect)) {
            throw new CustomException(ResultCodeEnum.COLLECTED_ALREADY_ERROR);
        }
        collectMapper.insert(collect);
    }

    public List<Type> selectOwn(Integer id) {
        List<Collect> list = collectMapper.selectByUserId(id);
        List<Type> result = new ArrayList<>();
        for (Collect collect : list) {
            Type type = typeMapper.selectById(collect.getTypeId());
            if (ObjectUtil.isNotEmpty(type)) {
                result.add(type);
            }
        }
        return result;
    }

    public void deleteByTypeId(Integer typeId) {
        collectMapper.deleteByTypeId(typeId);
    }
}
