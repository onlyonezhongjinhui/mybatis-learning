package com.demo.sql.enums;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface TableId {

    /**
     * 字段名(该值可无)
     */
    String value() default "";

}
