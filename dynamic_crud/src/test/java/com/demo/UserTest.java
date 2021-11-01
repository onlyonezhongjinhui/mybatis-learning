package com.demo;

import com.demo.entity.User;
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
        userMapper.insert(user);

        User selectUser = userMapper.selectById(user);
        System.out.println("selectUser1:" + selectUser.toString());

        user.setName("鸡鸡");
        user.setAge(31);
        userMapper.updateById(user);

        selectUser = userMapper.selectById(user);
        System.out.println("selectUser2:" + selectUser.toString());

        userMapper.deleteById(user);

        selectUser = userMapper.selectById(user);
        System.out.println("selectUser3:" + selectUser);
    }

}
