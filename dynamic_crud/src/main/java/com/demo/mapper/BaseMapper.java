package com.demo.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface BaseMapper<T> {

    @Insert("${sql}")
    void insert(@Param("sql") String sql);

    @Update("${sql}")
    void update(@Param("sql") String sql);

    @Delete("${sql}")
    void delete(@Param("sql") String sql);

    @Select("${sql}")
    T select(@Param("sql") String sql);

}
