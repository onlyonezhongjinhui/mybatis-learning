package com.demo.mapper;

import com.demo.entity.Goods;
import com.demo.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

}
