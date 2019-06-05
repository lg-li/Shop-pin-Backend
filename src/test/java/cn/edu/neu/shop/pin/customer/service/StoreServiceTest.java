package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.PinApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= PinApplication.class)
@EnableAutoConfiguration
public class StoreServiceTest {

    @Autowired
    StoreService storeService;

    @Test
    public void getStoreInfo() {
    }

    @Test
    public void getStoreListByOwnerId() {
        System.out.println(storeService.getStoreListByOwnerId(22));
    }
}