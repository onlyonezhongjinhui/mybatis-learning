package com.demo.entity;

import com.demo.mybatis.enums.TableId;
import com.demo.mybatis.enums.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName(value = "t_user")
public class User {
    @TableId(value = "id")
    private String id;
    private String name;
    private Integer age;
}
