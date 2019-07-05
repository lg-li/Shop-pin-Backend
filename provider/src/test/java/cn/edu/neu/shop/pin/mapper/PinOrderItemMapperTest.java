//package cn.edu.neu.shop.pin.mapper;
//
//import cn.edu.neu.shop.pin.mapper.PinOrderItemMapper;
//import org.junit.TestController;
//import org.junit.runner.RunWith;
//import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@MybatisTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class PinOrderItemMapperTest {
//
//    @Autowired
//    private PinOrderItemMapper pinOrderItemMapper;
//
//    @TestController
//    @Rollback(false)
//    public void testAddOrderItem() {
//        pinOrderItemMapper.createOrderItem(1, 1, 150, 1);
//    }
//}
