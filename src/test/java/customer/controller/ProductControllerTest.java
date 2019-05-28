package customer.controller;

import cn.edu.neu.shop.pin.mapper.PinUserMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductControllerTest {
    @Autowired
    PinUserMapper pinUserMapper;

    @Test
    public void MyTest(){
        System.out.println(pinUserMapper.findByEmail("liyifei_1999@163.com"));
    }
}
