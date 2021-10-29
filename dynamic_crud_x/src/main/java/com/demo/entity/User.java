package com.demo.entity;

import com.demo.sql.enums.TableId;
import com.demo.sql.enums.TableName;
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
