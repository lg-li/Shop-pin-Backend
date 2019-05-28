package cn.edu.neu.shop.pin.mapper;

import static org.junit.Assert.*;

import cn.edu.neu.shop.pin.model.PinUser;
import cn.edu.neu.shop.pin.model.PinUserProductCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinUserMapperTest {
    @Autowired
    PinUserMapper pinUserMapper;

    @Test
    public void findByEmail() {
        System.out.println();
        System.out.println("##################################################");
        System.out.println();
        PinUser p = pinUserMapper.findByEmail("liyifei_1999@163.com");
        System.out.println("id: " + p.getId());
        System.out.println("nickname: " + p.getNickname());
        System.out.println("gender: " + p.getGender());
        System.out.println("Email: " + p.getEmail());
        System.out.println("createTime: " + p.getCreateTime());
        System.out.println();
        System.out.println("##################################################");
        System.out.println();
    }

    @Test
    public void existsByEmail() {
    }

    @Test
    public void save() {
    }

    @Test
    public void deleteByEmail() {
    }
}