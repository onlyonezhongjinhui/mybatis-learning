package com.demo.mybatis.mapper;

import com.demo.mybatis.BaseSqlProvider;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BaseMapper<T> {

    @InsertProvider(type = BaseSqlProvider.class, method = "buildInsertSql")
    void insert(T entity);

    @UpdateProvider(type = BaseSqlProvider.class, method = "buildUpdateSql")
    void updateById(T entity);

    @DeleteProvider(type = BaseSqlProvider.class, method = "buildDeleteSql")
    void deleteById(T entity);

    @SelectProvider(type = BaseSqlProvider.class, method = "buildSelectSql")
    T selectById(T entity);

}
