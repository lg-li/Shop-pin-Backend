package cn.edu.neu.shop.pin.mapper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinUserMapperTest {
    @Autowired
    PinUserMapper pinUserMapper;

    @Test
    public void findByEmail() {
        System.out.println(pinUserMapper.findByEmail("liyifei_1999@163.com"));
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