package com.demo.mapper;

import com.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    void insert(User user);

    void updateById(User user);

    void deleteById(@Param("id") String id);

    User selectById(@Param("id") String id);

}
