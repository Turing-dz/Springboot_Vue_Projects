package com.example.mapper;

import com.example.entity.Comment;
import com.example.entity.Hotel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentMapper {
    void insert(Comment comment);//增
    void deleteById(Integer id);//删
    void updateById(Comment comment);//改

    List<Comment> selectAll(Comment comment);//查所有
    Comment selectById(Integer id);
    @Select("select * from comment where type_id=#{typeId} and parent_id=#{parentId}")
    List<Comment> selectByTypeIdAndParentId(@Param("typeId") Integer typeId,@Param("parentId") Integer parentId);
}
