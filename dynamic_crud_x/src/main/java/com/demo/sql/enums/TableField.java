package com.demo.sql.enums;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface TableField {

    /**
     * 数据库字段值
     */
    String value() default "";

}