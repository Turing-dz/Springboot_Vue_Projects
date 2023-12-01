package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.StatusEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import com.example.mapper.CommentMapper;
import com.example.mapper.HotelMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.el.parser.Token;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private HotelMapper hotelMapper;
    @Resource
    private UserMapper userMapper;
    /**
     * 新增
     */
    public void add(Comment comment){
        comment.setTime(DateUtil.now());
        commentMapper.insert(comment);
    }
    /**
     * 删除
     */
    public void deleteById(Integer id){
        commentMapper.deleteById(id);
    }
    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids){
        for (Integer id :ids){
            commentMapper.deleteById(id);
        }
    }
    /**
     * 修改
     */
    public void updateById(Comment comment){
        commentMapper.updateById(comment);
    }
    /**
     * 根据ID查询
     */
    public Comment selectById(Integer id){
        return commentMapper.selectById(id);
    }
    /**
     * 查询所有
     */
    public List<Comment> selectAll(Comment comment){
        return commentMapper.selectAll(comment);
    }
    /**
     * 分页查询
     */
    public PageInfo<Comment> selectPage(Comment comment,Integer pageNum,Integer pageSize){
        Account currentUser= TokenUtils.getCurrentUser();
        if (RoleEnum.HOTEL.name().equals(currentUser.getRole())){//如果是酒店分页查询 只能查到自己酒店的 评论
            comment.setHotelId(currentUser.getId());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Comment> list =commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }
    /**
     * 根据酒店Type查询
     */
    private void extracted(Comment comment){
        if (RoleEnum.ADMIN.name().equals(comment.getRole())){
            Admin admin=adminMapper.selectById(comment.getUserId());
            comment.setAvatar(admin.getAvatar());
            comment.setUserName(admin.getName());
        }
        if(RoleEnum.HOTEL.name().equals((comment.getRole()))){
            Hotel hotel =hotelMapper.selectById(comment.getUserId());
            comment.setAvatar(hotel.getAvatar());
            comment.setUserName(hotel.getName());
        }
        if(RoleEnum.USER.name().equals((comment.getRole()))){
            User user =userMapper.selectById(comment.getUserId());
            comment.setAvatar(user.getAvatar());
            comment.setUserName(user.getName());
        }
    }
    public List<Comment> selectByTypeId(Integer id){
        List<Comment> list=commentMapper.selectByTypeIdAndParentId(id,0);
        for (Comment comment:list){
            extracted(comment);
            List<Comment> commentList=commentMapper.selectByTypeIdAndParentId(id,comment.getId());
            for (Comment cmmt:commentList){
                extracted(cmmt);
            }
            comment.setChildren(commentList);

        }
        return list;
    }
}
