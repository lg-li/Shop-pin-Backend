package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.service.OrderIndividualService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderIndividualServiceTest {

    @Autowired
    OrderIndividualService orderIndividualService;

    @Test
    public void testUpdateProductStatusNotExpress() {

    }
}
