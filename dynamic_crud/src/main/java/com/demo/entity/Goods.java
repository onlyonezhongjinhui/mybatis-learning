package com.demo.entity;

import com.demo.mybatis.enums.TableId;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Goods {
    @TableId
    private Integer goodsId;
    private String goodsTitle;
    private Integer goodsCount;
}
