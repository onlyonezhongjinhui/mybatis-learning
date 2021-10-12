package com.demo;

import com.demo.entity.User;
import com.demo.enums.Education;
import com.demo.enums.Gender;
import com.demo.enums.Nation;
import com.demo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("叽叽");
        user.setAge(30);
        user.setGender(Gender.MAN);
        user.setNation(Nation.H);
        user.setEducation(Education.PRIMARY_SCHOOL);
        userMapper.insert(user);

        user = userMapper.selectById(user.getId());

        user.setEducation(Education.JUNIOR_MIDDLE_SCHOOL);
        userMapper.updateById(user);

        user = userMapper.selectById(user.getId());

        System.out.println(user);
    }

}
