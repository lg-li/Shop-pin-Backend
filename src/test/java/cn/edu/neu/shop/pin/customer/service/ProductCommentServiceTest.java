package cn.edu.neu.shop.pin.customer.service;

import cn.edu.neu.shop.pin.service.ProductCommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductCommentServiceTest {

    @Autowired
    private ProductCommentService productCommentService;

    @Test
    public void testGetComments(){
        Integer list[] = productCommentService.getComments(9);
        for(Integer i: list){
            System.out.println(i);
        }
    }
}
