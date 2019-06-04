package cn.edu.neu.shop.pin.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinOrderIndividualMapperTest {

    @Autowired
    PinOrderIndividualMapper mapper;

    @Test
    public void selectByOrderGroupId() {
        System.out.println(mapper.selectByOrderGroupId(1));
        System.out.println(mapper.selectByOrderGroupId(1).size());
    }
}