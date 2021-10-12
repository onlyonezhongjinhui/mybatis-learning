package com.demo.entity;

import com.demo.enums.Education;
import com.demo.enums.Gender;
import com.demo.enums.Nation;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    private String id;
    private String name;
    private Integer age;
    private Gender gender;
    private Nation nation;
    private Education education;
}
