package cn.edu.neu.shop.pin.mapper;

import cn.edu.neu.shop.pin.service.OrderGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderGroupServiceTest {

    @Autowired
    private OrderGroupService orderGroupService;

    @Test
    public void testGetOrderGroupCloseTimeFromNow() {
        System.out.println(orderGroupService.getOrderGroupCloseTimeFromNow(1));
    }
}
